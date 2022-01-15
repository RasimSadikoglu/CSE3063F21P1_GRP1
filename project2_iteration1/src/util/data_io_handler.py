import sys
import json
import os
import shutil
from course.course import Course


def readCourseFile():
    with open(f'{sys.path[0]}/../files/courses.json', 'r', encoding='utf-8') as coursesFile:
        courses = json.load(coursesFile)

        courses = [Course(**c) for c in courses]

        for course in courses:
            coursePrerequisites = course.prerequisites

            if len(coursePrerequisites) == 0:
                continue

            prerequisiteCourses = []

            for preq in coursePrerequisites:
                preq, note = preq.split('-')

                preqCourse = next(filter(lambda c: c.code == preq, courses))

                prerequisiteCourses.append(
                    (preqCourse, 1 if note == 'P' else 0.5))

            course.prerequisites = prerequisiteCourses

        return courses


def readStudentFiles():  # will be implemented later.
    return []


def saveStudentFiles(students: list):

    if os.path.exists(f'{sys.path[0]}/../files/students'):
        shutil.rmtree(f'{sys.path[0]}/../files/students')

    os.mkdir(f'{sys.path[0]}/../files/students')

    for s in students:
        with open(f'{sys.path[0]}/../files/students/{s.id}.json', 'w') as studentFile:
            json.dump(s.__dict__(), studentFile, ensure_ascii=False, indent=4)


def readsimulationParameters():
    with open(f'{sys.path[0]}/../files/simulationParameters.json', 'r', encoding='utf-8') as coursesFile:
        return json.load(coursesFile)

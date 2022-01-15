import sys, json, os, shutil
from course.course import Course

def getPath() -> str:

    if os.path.exists(f'{sys.path[0]}/../files'):
        return f'{sys.path[0]}/../files'
    else:
        return f'{sys.path[0]}/files'

def readCourseFile():

    path = getPath()

    with open(f'{path}/courses.json', 'r', encoding='utf-8') as coursesFile:
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

def saveStudentFiles(students: list):

    path = getPath()

    if os.path.exists(f'{path}/students'):
        shutil.rmtree(f'{path}/students')

    os.mkdir(f'{path}/students')

    for s in students:
        with open(f'{path}/students/{s.id}.json', 'w') as studentFile:
            json.dump(s.__dict__(), studentFile, ensure_ascii=False, indent=4)

def readsimulationParameters():
    path = getPath()

    with open(f'{path}/simulationParameters.json', 'r', encoding='utf-8') as coursesFile:
        return json.load(coursesFile)

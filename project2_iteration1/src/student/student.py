from system.advisor import Advisor
from course.course_registration import CourseRegistration
from .transcript import Transcript
import random

class Student:
    
    def __init__(self, id: str, name: str, surname: str, successChance: float, advisor: Advisor, transcript: Transcript = None):

        self.id = id
        self.name = name
        self.surname = surname
        self.successChance = successChance
        self.advisor = advisor
        self.currentSchedule = []
        self.transcript = transcript if transcript != None else Transcript()

    @property
    def fullName(self) -> str:
        return self.name + ' ' + self.surname

    @property
    def currentSemester(self) -> int:
        return self.transcript.currentSemester

    @property
    def email(self) -> str:
        return f'{self.name.lower()}.{self.surname.lower()}@marun.edu.tr'

    def registration(self, managementSystem):
        courseRegistration: CourseRegistration = managementSystem.getCatalog(self)

        for courseList in courseRegistration.catalog:

            courseList = list(filter(lambda c: c['course'] not in courseRegistration.blacklist, courseList))

            if len(courseList) == 0:
                continue

            random.shuffle(courseList)

            course = courseList[0]['course']
            
            availableSections = course.getAvailableCourseSections(courseRegistration.blacklist)

            if len(availableSections) == 0:
                print('quota')
                courseRegistration.blacklist.append(course)
                continue

            randomCourseSection = random.randint(0, len(availableSections) - 1)

            courseRegistration.blacklist.append(course)

            courseRegistration.courses.append(course)
            courseRegistration.studentSchedule.append(availableSections[randomCourseSection])

        managementSystem.checkRegistration(courseRegistration)

    def completeRegistration(self, semester: list):
        self.transcript.completeRegistration(semester)

    def getCourseInfo(self, course) -> dict:
        return self.transcript.getCourseInfo(course)

    def getCourseCount(self, courseGroup) -> int:
        return self.transcript.getCourseCount(courseGroup)

    def updateCourseNote(self, course, note):
        self.transcript.updateCourseNote(course, note)

    def __dict__(self) -> dict:
        self.transcript.updateGPA()

        return {
            'ID': self.id,
            'Name': self.fullName,
            'Advisor': self.advisor.fullName,
            'GPA': self.transcript.GPA,
            'Completed Credits': self.transcript.completedCredits,
            'transcript': self.transcript.__dict__()
        }
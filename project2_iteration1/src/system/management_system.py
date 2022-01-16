from .advisor import Advisor
from course.course import Course
from course.course_registration import CourseRegistration
from student.student import Student
import logging

class ManagementSystem:

    electiveCurriculum = [
        [],
        ['NTE-UE1'],
        ['NTE-UE2'],
        ['NTE-UE2'],
        ['NTE-UE2'],
        ['NTE-UE2'],
        ['TE2', 'NTE-UE3'],
        ['FTE1', 'TE5', 'NTE-UE4']
    ]

    semesters = ['Spring', 'Fall']

    def __init__(self, courses: list[Course], advisors: list[Advisor], currentSemester: int, students: list[Student] = []):
        self.courses = courses
        self.students = students
        self.advisors = advisors

        self.currentSemester = currentSemester

    def registerNewStudents(self, students: list[Student]):
        self.students.extend(students)

    def startRegistration(self):
        
        for student in self.students:
            student.registration(self)

    def getCatalog(self, student: Student) -> CourseRegistration:

        currentSemesterOfStudent = min(student.currentSemester - 1, 7)

        catalog = []

        for i in range(1, currentSemesterOfStudent + 2):
            courseGroup = 'SME' + str(i)

            studentCourseInfos = [[student.getCourseInfo(course)] for course in self.courses if course.group == courseGroup and course.semester == self.semesters[self.currentSemester % 2]]

            catalog.extend(list(filter(lambda courseInfo: courseInfo[0]['lastNote'] == None or courseInfo[0]['lastNote'] < 1, studentCourseInfos)))

        for courseGroup in self.electiveCurriculum[currentSemesterOfStudent]:
            
            courseCount = int(courseGroup[-1]) - student.getCourseCount(courseGroup[:-1])
            courseGroup = courseGroup[:-1]

            studentCourseInfos = [student.getCourseInfo(course) for course in self.courses if course.group == courseGroup and course.semester == self.semesters[self.currentSemester % 2]]
            studentCourseInfos = [list(filter(lambda courseInfo: courseInfo['lastNote'] == None or courseInfo['lastNote'] < 1, studentCourseInfos))] * courseCount

            catalog.extend(studentCourseInfos)

        return CourseRegistration(student, catalog)

    def checkRegistration(self, courseRegistration: CourseRegistration):

        student = courseRegistration.student
        advisorOfStudent = student.advisor

        courseRegistration.courses = courseRegistration.courses[:10]

        courses = courseRegistration.courses
        schedule = courseRegistration.studentSchedule

        i = 0
        while i < len(courses):

            satisfy = True

            for preq, reqNote in courses[i].prerequisites:
                studentCourseInfo = student.getCourseInfo(preq)

                if studentCourseInfo['lastNote'] == None or studentCourseInfo['lastNote'] < reqNote:
                    logging.warning(f'[SYSTEM] [PREREQUISITE] [{student.id}] Student couldn\'t register course {courses[i].code} because of the prerequisite {preq.code}.')
                    satisfy = False
                    break

            if satisfy:
                i += 1
            else:
                del courses[i]
                del schedule[i]

        advisorOfStudent.checkRegistration(courseRegistration)

    def endSemester(self):
        
        for course in self.courses:
            course.generateExamScores()
            course.clear()

        for student in self.students:
            student.transcript.updateGPA()

        self.currentSemester += 1
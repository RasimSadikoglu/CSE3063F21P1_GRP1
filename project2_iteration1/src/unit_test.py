from course.course import Course
from student.student import Student
from util.person_generator import createRandomStudents, createRandomAdvisors
from util.data_io_handler import readCourseFile
from course.course_registration import CourseRegistration
from system.management_system import ManagementSystem

def test_managementSystemMustNotDropCourse():
    managementSystem = ManagementSystem([], [], 1, [])
    
    student = getStudentWithCourses()
    courseRegistration = getCourseRegistration(student, [getCourse('CSE2023')])
    
    managementSystem.checkRegistration(courseRegistration)

    assert checkForCourse(student, 'CSE2023')

def test_managementSystemMustDropCourse():
    managementSystem = ManagementSystem([], [], 1, [])
    
    student = getStudentWithCourses()
    courseRegistration = getCourseRegistration(student, [getCourse('CSE2025')])
    
    managementSystem.checkRegistration(courseRegistration)

    assert not checkForCourse(student, 'CSE2025')

def test_gpaCalculationMustBeCorrect():
    student = getStudentWithCourses()

    student.transcript.updateGPA()

    assert student.transcript.gpa == 1.95

def test_studentCourseNoteMustBeCorrect():
    student = getStudentWithCourses()

    note = student.getCourseInfo(getCourse('CSE1100'))['lastNote']

    assert note == 2.5

def test_studentMustNotHaveANoteFromACourseHeDoesntTake():
    student = getStudentWithCourses()

    note = student.getCourseInfo(getCourse('CSE2023'))['lastNote']

    assert note == None

def test_studentMustHaveCorrectCourseCount():
    student = getStudentWithCourses()

    count = student.getCourseCount('SME1')

    assert count == 4

def test_advisorMustRejectTECourseWhenStudentDoesNotHaveEnoughCredits():
    student = getStudentWithCourses()

    student.transcript.completedCredits = 100

    courseRegistration = getCourseRegistration(student, [getCourse('CSE4082')])
    student.advisor.checkRegistration(courseRegistration)

    assert not checkForCourse(student, 'CSE4082')

def test_advisorMustNotRejectTECourseWhenStudentHasEnoughCredits():
    student = getStudentWithCourses()

    student.transcript.completedCredits = 200

    courseRegistration = getCourseRegistration(student, [getCourse('CSE4082')])
    student.advisor.checkRegistration(courseRegistration)

    assert checkForCourse(student, 'CSE4082')

def test_advisorMustRejectMoreThanTwoTECoursesInTheFallSemester():
    student = getStudentWithCourses()
    student.transcript.semesters.append([])

    student.transcript.completedCredits = 200

    courseRegistration = getCourseRegistration(student, [getCourse('CSE4082'), getCourse('CSE4083'), getCourse('CSE4084')])
    
    student.advisor.checkRegistration(courseRegistration)

    student.updateCourseNote(getCourse('CSE4082'), 4.0)
    student.updateCourseNote(getCourse('CSE4083'), 4.0)
    student.updateCourseNote(getCourse('CSE4084'), 4.0)

    count = student.getCourseCount('TE')

    assert count == 2

def test_advisorMustRejectFTECoursesIfStudentDoesntGraduateInTheFallSemester():
    student = getStudentWithCourses()
    student.transcript.semesters.append([])

    student.transcript.completedCredits = 200

    courseRegistration = getCourseRegistration(student, [getCourse('MGT4084')])

    student.advisor.checkRegistration(courseRegistration)

    assert not checkForCourse(student, 'MGT4084')

def test_advisorMustNotRejectFTECoursesIfStudentDoesntGraduateInTheFallSemester():
    student = getStudentWithCourses()
    student.transcript.semesters.append([])

    student.transcript.completedCredits = 235

    courseRegistration = getCourseRegistration(student, [getCourse('MGT4084')])

    student.advisor.checkRegistration(courseRegistration)

    assert checkForCourse(student, 'MGT4084')

def test_studentIdsMustBeUnique():
    advisors = createRandomAdvisors(10)
    students = createRandomStudents(advisors, 2020, 100)

    assert len(students) == 100
    studentIds = set()
    for student in students:
        assert not student.id in studentIds
        studentIds.add(student.id)


# HELPER FUNCTIONS

def checkForCourse(student: Student, course: str) -> bool:
    transcript = student.transcript.__dict__()
    
    for semester in transcript:
        if course in semester:
            return True

    return False 

def getStudentWithCourses() -> Student:
    advisor = createRandomAdvisors(1)
    student: Student = createRandomStudents(advisor, 2020, 1)[0]

    allCourses = readCourseFile()

    courseCodes = ['ATA121', 'CSE1100', 'CSE1141', 'MATH1001', 'PHYS1101']

    courses = list(filter(lambda c: c.code in courseCodes and c.semester == 'Fall', allCourses))

    courseRegistration = getCourseRegistration(student, courses)
    courseRegistration.completeRegistration()

    student.updateCourseNote(courses[0], 4.0)
    student.updateCourseNote(courses[1], 2.5)
    student.updateCourseNote(courses[2], 1.5)
    student.updateCourseNote(courses[3], 0.5)
    student.updateCourseNote(courses[4], 3.5)

    return student

def getCourseRegistration(student: Student, courses: list):
    courseRegistration = CourseRegistration(student, [])

    courseRegistration.courses = courses
    courseRegistration.studentSchedule = [course.getAvailableCourseSections([])[0] for course in courses]

    return courseRegistration

def getCourse(courseCode: str) -> Course:
    allCourses = readCourseFile()

    return next(filter(lambda c: c.code == courseCode, allCourses))
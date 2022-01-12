from student.semester import Semester


class LetterNote(enumerate):
    AA = 4.0
    BA = 3.5
    BB = 3.0
    CB = 2.5
    CC = 2.0
    DC = 1.5
    DD = 1.0
    FD = 0.5
    FF = 0.0
    DZ = -1.0

class Transcript:
    
    def __init__(self, semesters: list = None):
        self.semesters = [] if semesters == None else semesters
        self.completedCredits = 0
        self.totalCredits = 0
        self.GPA = 0 

    @property
    def currentSemester(self) -> int:
        return len(self.semesters) + 1

    @property
    def GPA(self) -> float:
        completedCourses = []
        points = 0
        for semester in reversed(self.semesters):
            for takenCourse,note in semester:
                courseName = takenCourse.name
                if completedCourses.__contains__(courseName): continue
                    
                completedCourses.__add__(courseName)
                if note >= 1: self.completedCredits += takenCourse.credits
                   
                self.totalCredits += takenCourse.credits
                points += 0 if note == -1 else takenCourse.credits * note
        self.GPA = points / self.totalCredits
        return self.GPA



    def completeRegistration(self, semester: list):
        self.semesters.append(semester)

    def getCourseInfo(self, course) -> dict:
        courseInfo = {
            'course': course,
            'registrationCount': 0,
            'lastNote': None
        }

        for semester in reversed(self.semesters):
            for takenCourse, note in semester:
                if course.code == takenCourse.code:
                    courseInfo['registrationCount'] += 1
                    
                    if courseInfo['lastNote'] == None:
                        courseInfo['lastNote'] = note

        return courseInfo

    def updateCourseNote(self, course, note):

        for i, (takenCourse, n) in enumerate(self.semesters[-1]):
            if takenCourse.code == course.code:
                self.semesters[-1][i] = (course, note)
                return

        raise ValueError('Course is not found!')

    def getCourseCount(self, courseGroup) -> int:
        count = 0

        courseCodes = []
        
        for semester in self.semesters:
            for course, note in semester:
                if course.group == courseGroup and note > 1 and course.code not in courseCodes:
                    courseCodes.append(course.code)
                    count += 1

        return count

    def __dict__(self):
        return [{c.code: note for c, note in semester} for semester in self.semesters]

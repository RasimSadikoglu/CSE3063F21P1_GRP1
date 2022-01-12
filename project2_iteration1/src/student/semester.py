class Semester:

    def __init__(self, notes:list):
        self.notes = notes
        self.completedCredits = 0
        self.totalCredits = 0
        self.semesterGPA = 0
        self.setCredits(notes)
        self.setSemesterGPA()
    
    def setCredits(self,notes):
        for takenCourse,note in notes:
            if note >= 1.0:
                self.completedCredits += takenCourse.credits
            self.totalCredits += takenCourse.credits 

    def setSemesterGPA(self):
        points = 0
        for takenCourse,note in self.notes:
            courseCredit = takenCourse.credits
            points += 0 if note == -1 else courseCredit * note

        self.semesterGPA = points / self.totalCredits
    

    


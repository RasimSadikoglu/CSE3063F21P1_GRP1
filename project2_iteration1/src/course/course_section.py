class CourseSection:

    def __init__(self, sectionCode: str, lecturer: str, quota: int, schedule: list, labSections: list = []):
        self.sectionCode = sectionCode
        self.lecturer = lecturer
        self.quota = quota
        self.schedule = schedule
        self.labSections = [CourseSection(**section) for section in labSections]

        self.registrable = True
        self.students = []

    def addStudent(self, student):
        self.students.append(student)

        if self.quota != 0 and self.quota <= len(self.students):
            self.registrable = False

    def clear(self):
        for section in self.labSections:
            section.clear()

        self.students.clear()
        self.registrable = True
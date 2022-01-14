class RegistrationStatus(enumerate):
    COMPLETE = 0
    DRAFT = 1
    # WAITING_ADVISOR_APPROVAL = 2
    # ADVISOR_APPROVED = 3

class CourseRegistration:

    def __init__(self, student, catalog: list):
        self.status = RegistrationStatus.DRAFT
        
        self.student = student
        self.catalog = catalog

        self.courses = []
        self.studentSchedule = []
        self.blacklist = []

    def completeRegistration(self):
        self.student.currentSchedule = self.studentSchedule
        self.student.completeRegistration([(course, None) for course in self.courses])
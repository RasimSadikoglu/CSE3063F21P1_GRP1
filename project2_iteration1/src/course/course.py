from .course_section import CourseSection
import random

notes = {
    4.0: 'AA',
    3.5: 'BA',
    3.0: 'BB',
    2.5: 'CB',
    2.0: 'CC',
    1.5: 'DC',
    1.0: 'DD',
    0.5: 'FD',
    0.0: 'FF'
}

class Course:

    def __init__(self, code: str, group: str, semester: str, name: str, credits: float,
                       requiredCredits: float, prerequisites: list, sections: list):
        self.code = code
        self.group = group
        self.semester = semester
        self.name = name
        self.credits = credits
        self.requiredCredits = requiredCredits
        self.prerequisites = prerequisites
        self.sections = [CourseSection(**section) for section in sections]

    def getAvailableCourseSections(self, blacklist: list[CourseSection]):
        availableSections = []

        for section in self.sections:
            if not section.registrable or section in blacklist:
                continue
            
            if len(section.labSections) == 0:
                availableSections.append((section, None))
                continue

            for labSection in section.labSections:
                if not labSection.registrable or section in blacklist:
                    continue

                availableSections.append((section, labSection))

        return availableSections

    def generateExamScores(self):
        
        students = []

        for section in self.sections:
            students.extend(section.students)

        # Random exam note generating
        for student in students:
            student.updateCourseNote(self, random.choice(list(notes.keys())))
        #

    def clear(self):

        for section in self.sections:
            section.clear()
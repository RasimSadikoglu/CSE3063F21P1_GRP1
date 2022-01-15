from util.person_generator import createRandomAdvisors, createRandomStudents
from util.data_io_handler import readCourseFile, readStudentFiles, readsimulationParameters, saveStudentFiles
from system.management_system import ManagementSystem
import logging

class Simulation:
    
    def __init__(self):
        
        self.parameters = readsimulationParameters()
        self.courses = readCourseFile()
        self.advisors = createRandomAdvisors(self.parameters['advisorAmount'])
        self.students = []

        self.currentSemester = 1
        self.currentYear = self.parameters['startYear']

        self.managementSystem = ManagementSystem(self.courses, self.advisors, self.currentSemester, self.students)

        if not self.parameters['ignoreOldStudentData']:
            self.students = readStudentFiles()

    def start(self):
        
        for i in range(self.parameters['loopCount']):

            if self.currentSemester % 2 == 1:
                newStudents = createRandomStudents(self.advisors, self.currentYear, self.parameters['yearlyStudentAmount'])

                self.managementSystem.registerNewStudents(newStudents)

            self.managementSystem.startRegistration()

            self.managementSystem.endRegistration()
            
            self.currentSemester += 1
            self.currentYear += self.currentSemester % 2

        saveStudentFiles(self.students)

def initializeLogger():
    logging.basicConfig(level=logging.DEBUG,
                        filename='logs.log',
                        filemode='w')
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    logging.getLogger().addHandler(console)

if __name__ == '__main__':
    initializeLogger()

    simulation = Simulation()
    simulation.start()
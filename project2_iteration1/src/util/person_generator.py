import sys
import random
from student.student import Student
from system.advisor import Advisor


def getNames():
    firstNames = []

    with open(f'{sys.path[0]}/files/firstNames.csv', 'r') as firstNamesFile:
        firstNames = firstNamesFile.read()

        firstNames = [name.split(',')[0] for name in firstNames.split('\n')]

    lastNames = []

    with open(f'{sys.path[0]}/files/lastNames.csv', 'r') as lastNamesFile:
        lastNames = lastNamesFile.read()

        lastNames = [name.split(',')[0] for name in lastNames.split('\n')]

    return firstNames, lastNames


def createRandomStudents(advisors: list[Advisor], year: int, amount: int):

    firstNames, lastNames = getNames()

    students = []

    for i in range(1, amount + 1):
        randomAdvisor = random.choice(advisors)

        student = Student(f'1501{year % 100:02}{i:03}', random.choice(
            firstNames), random.choice(lastNames), 0, randomAdvisor, None)
        randomAdvisor.students.append(student)

        students.append(student)

    return students


def createRandomAdvisors(amount: int):

    firstNames, lastNames = getNames()

    return [Advisor(random.choice(firstNames), random.choice(lastNames)) for i in range(amount)]

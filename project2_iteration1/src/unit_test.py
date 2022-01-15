import pytest
from student.student import Student
from util.person_generator import createRandomStudents, createRandomAdvisors
import logging


def initializeLogger():
    logging.basicConfig(level=logging.DEBUG,
                        filename='logs.log',
                        filemode='w')
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    logging.getLogger().addHandler(console)


initializeLogger()

logging.info('Tests')


def test_managementSystemMustNotDropCourse():
    advisors = createRandomAdvisors(10)
    student = createRandomStudents(advisors, 2020, 1)[0]


def test_studentIdsMustBeUnique():
    advisors = createRandomAdvisors(10)
    students = createRandomStudents(advisors, 2020, 100)

    assert len(students) == 100
    studentIds = set()
    for student in students:
        assert not student.id in studentIds
        studentIds.add(student.id)


def test_():
    pass

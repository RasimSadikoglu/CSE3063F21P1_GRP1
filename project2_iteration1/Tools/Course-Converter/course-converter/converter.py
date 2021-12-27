import pandas, sys, json, re, functools

def parseCourses(filePath: str) -> list:

    excelFile = pandas.read_excel(filePath).iloc()

    courses = []

    for row in excelFile:
        courses.append(
            {
                'courseCode': str(row[0]),
                'courseGroup': '',
                'courseName': str(row[1]),
                'lecturer': str(row[2]),
                'credit': float(row[5]),
                'quota': int(row[7]),
                'schedule': parseTime(str(row[8])),
                'prerequisite': []
            }
        )

    for course in courses:
        course['courseCode'] = updateCourseCode(course)
        course['credit'] = updateCourseCredit(course)
        course['prerequisite'] = updateCoursePrerequisite(course)

    return courses

def mergeCourses(courses: list) -> list:
    
    mergedCourses = []

    for course in filter(lambda x: re.match('^\\w+\\.\\d+$', x['courseCode']) != None, courses):
        courseCode = course['courseCode'].split('.')[0]

        parentCourse = list(filter(lambda x: x['courseCode'] == courseCode, mergedCourses))

        if len(parentCourse) == 0:
            mergedCourses.append({
                'courseCode': courseCode,
                'courseGroup': course['courseGroup'],
                'courseName': course['courseName'],
                'credit': course['credit'],
                'prerequisite': course['prerequisite'],
                'sections': []
            })
            parentCourse = mergedCourses[-1]
        else:
            parentCourse = parentCourse[0]

        parentCourse['sections'].append({
            'courseSection': course['courseCode'],
            'lecturer': course['lecturer'],
            'quota': course['quota'],
            'schedule': course['schedule'],
            'labSections': []
        })

    for course in filter(lambda x: re.match('^\\w+\\.\\d+\\.\\d+$', x['courseCode']) != None, courses):
        courseCode = course['courseCode'].split('.')[0]
        courseSection = course['courseCode'].rsplit('.', maxsplit=1)[0]

        parentCourse = list(filter(lambda x: x['courseCode'] == courseCode, mergedCourses))[0]['sections']
        parentSection = list(filter(lambda x: x['courseSection'] == courseSection, parentCourse))[0]

        parentSection['labSections'].append({
            'labSection': course['courseCode'],
            'lecturer': course['lecturer'],
            'quota': course['quota'],
            'schedule': course['schedule'],
        })

    return mergedCourses

def filterCourses(courses: list) -> list:
    curriculumPath = __file__.rsplit('\\', maxsplit=2)[0] + '/files/curriculum.json'

    curriculum = {}

    with open(curriculumPath, 'r') as curFile:
        curriculum = json.load(curFile)

    filteredCourses = []

    for course in courses:
        for courseGroup in curriculum:
            if course['courseCode'] in curriculum[courseGroup]:
                filteredCourses.append(course.copy())
                filteredCourses[-1]['courseGroup'] = courseGroup

    return sorted(filteredCourses, key=lambda x: (x['courseGroup'], x['courseCode']))

def updateCourseCode(course: dict) -> str:
    renameList = {
        'CSE2046': 'CSE2246',
        'STAT2053': 'STAT2253',
        'CSE3015': 'CSE3215',
        'CSE3064': 'CSE3264',
        'IE3035': 'IE3235',
        'CSE4197': 'CSE4297',
        'CSE4198': 'CSE4298'
    }

    courseCode = course['courseCode'].split('.', maxsplit=1)

    if courseCode[0] in renameList:
        return renameList[courseCode[0]] + '.' + courseCode[1]
    else:
        return course['courseCode']

def updateCourseCredit(course: dict) -> float:
    newCredits = {
        'STAT2253': 5.0,
        'CSE3215': 6.0,
        'CSE4297': 5.0,
        'CSE4298': 5.0
    }

    courseCode = course['courseCode'].split('.', maxsplit=1)

    if courseCode[0] in newCredits:
        return newCredits[courseCode[0]]
    else:
        return course['credit']

def updateCoursePrerequisite(course: dict) -> list:
    prerequisites = {
        'CSE1241': ['CSE1241-C'],
        'MATH2059': ['MATH1001-C'],
        'CSE2225': ['CSE1242-P'],
        'EE2031': ['PHYS1102-C'],
        'CSE2246': ['CSE2225-P'],
        'CSE2260': ['CSE1242-C'],
        'EE2032': ['EE2031-C'],
        'CSE3055': ['CSE2225-C'],
        'CSE3033': ['CSE2225-C'],
        'CSE3063': ['CSE1242-C'],
        'IE3081': ['STAT2253-C'],
        'CSE3044': ['CSE3055-C'],
        'CSE3264': ['CSE2023-C'],
        'CSE3038': ['CSE3215-C'],
        'CSE3048': ['MATH2055-C'],
        'IE3235': ['MATH2256-C'],
        'CSE4219': ['CSE3038-C'],
        'CSE4288': ['MATH2256-C', 'STAT2253-C'],
        'CSE4298': ['CSE4297-P'],
        'CSE4075': ['CSE4074-P'],
        'CSE4034': ['CSE3033-P'],
        'CSE4061': ['CSE3264-P'],
        'CSE4217': ['CSE3038-C']
    }

    courseCode = course['courseCode'].split('.')[0]

    return prerequisites[courseCode] if courseCode in prerequisites else []

def parseTime(schedule: str) -> list:
    days = [u'Pazartesi', u'Salı', u'Çarşamba', u'Perşembe', u'Cuma', u'Cumartesi', u'Pazar']

    scheduleList = []

    for course in list(filter(lambda x: x != 'nan' and x != '', schedule.split('] '))):

        parsedTime = list(filter(lambda x: x != '-' and x != '', course.split(' ')))[:3]

        scheduleList.append(f'{days.index(parsedTime[0])}-{parsedTime[1]}-{parsedTime[2]}')

    return scheduleList

def writeFile(filePath: str, courses: list):
    
    with open(filePath, 'w', encoding='utf-8') as out:
        json.dump(courses, out, indent=4, ensure_ascii=False)


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print('Error!')
        exit(1)

    filePath = sys.argv[1]

    courses = parseCourses(filePath)

    courses = mergeCourses(courses)

    courses = filterCourses(courses)

    writeFile(filePath.rsplit('.', maxsplit=1)[0] + '.json', courses)
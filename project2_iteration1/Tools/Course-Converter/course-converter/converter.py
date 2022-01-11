import pandas, json, re

def parseCourses(excelFile) -> list:

    courses = []

    for row in excelFile:
        courses.append({
            'courseCode': str(row[0]),
            'courseName': str(row[1]),
            'lecturer': str(row[2]),
            'credit': float(row[5]),
            'quota': int(row[7]),
            'schedule': parseTime(str(row[8])),
            'prerequisite': []
        })

    return courses

def mergeCourses(courses: list, semester: str) -> list:
    
    mergedCourses = []

    for course in filter(lambda x: re.match('^\\w+\\.\\d+$', x['courseCode']) != None, courses):
        courseCode = course['courseCode'].split('.')[0]

        parentCourse = list(filter(lambda x: x['code'] == courseCode, mergedCourses))

        if len(parentCourse) == 0:
            mergedCourses.append({
                'code': courseCode,
                'group': '',
                'semester': semester,
                'name': course['courseName'],
                'credits': course['credit'],
                'requiredCredits': 0,
                'prerequisites': course['prerequisite'],
                'sections': []
            })
            parentCourse = mergedCourses[-1]
        else:
            parentCourse = parentCourse[0]

        parentCourse['sections'].append({
            'sectionCode': course['courseCode'],
            'lecturer': course['lecturer'],
            'quota': course['quota'],
            'schedule': course['schedule'],
            'labSections': []
        })

    for course in filter(lambda x: re.match('^\\w+\\.\\d+\\.\\d+$', x['courseCode']) != None, courses):
        courseCode = course['courseCode'].split('.')[0]
        courseSection = course['courseCode'].rsplit('.', maxsplit=1)[0]

        parentCourse = list(filter(lambda x: x['code'] == courseCode, mergedCourses))[0]['sections']
        parentSection = list(filter(lambda x: x['sectionCode'] == courseSection, parentCourse))[0]

        parentSection['labSections'].append({
            'sectionCode': course['courseCode'],
            'lecturer': course['lecturer'],
            'quota': course['quota'],
            'schedule': course['schedule'],
            'labSections': []
        })

    return mergedCourses

def filterCourses(courses: list) -> list:
    curriculum = {}

    with open('files/curriculum.json', 'r') as curFile:
        curriculum = json.load(curFile)

    filteredCourses = []

    for course in courses:
        for courseGroup in curriculum:
            if course['code'] in curriculum[courseGroup]:
                filteredCourses.append(course.copy())
                filteredCourses[-1]['group'] = courseGroup

    return filteredCourses

def updateCoursePrerequisite(courses: list):
    prerequisites = {
        'CSE1142': ['CSE1141-C'],
        'MATH2055': ['MATH1001-C'],
        'CSE2025': ['CSE1142-P'],
        'EE2031': ['PHYS1102-C'],
        'MATH2059': ['MATH1001-C'],
        'CSE2046': ['CSE2025-P'],
        'EE2032': ['EE2031-C'],
        'CSE3055': ['CSE2025-C'],
        'CSE3033': ['CSE2025-C'],
        'CSE3063': ['CSE1142-C'],
        'IE3081': ['STAT2053-C'],
        'CSE3048': ['MATH2055-C'],
        'CSE3044': ['CSE3055-C'],
        'CSE3064': ['CSE2023-C'],
        'CSE3038': ['CSE2138-C'],
        'IE3035': ['MATH2056-C'],
        'CSE4117': ['CSE3038-C'],
        'CSE4198': ['CSE4197-P'],
        'CSE4075': ['CSE4074-P'],
        'CSE4054': ['CSE3055-P'],
        'CSE4034': ['CSE3033-P'],
        'CSE4061': ['CSE3064-P'],
        'CSE4087': ['CSE3015-P']
    }

    for course in courses:
        if course['code'] in prerequisites:
            course['prerequisites'] = prerequisites[course['code']]

        if course['group'] == 'TE':
            course['requiredCredits'] = 165

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

    excel_file = pandas.read_excel('files/fall.xlsx').iloc()

    fallParsedCourses = parseCourses(excel_file)

    fallMergedCourses = mergeCourses(fallParsedCourses, 'Fall')

    fallFilteredCourses = filterCourses(fallMergedCourses)

    updateCoursePrerequisite(fallFilteredCourses)

    excel_file = pandas.read_excel('files/spring.xlsx').iloc()

    springParsedCourses = parseCourses(excel_file)

    springMergedCourses = mergeCourses(springParsedCourses, 'Spring')

    springFilteredCourses = filterCourses(springMergedCourses)

    updateCoursePrerequisite(springFilteredCourses)

    courses = sorted(fallFilteredCourses + springFilteredCourses, key=lambda x: (x['group'], x['code']))

    with open('files/courses.json', 'w', encoding='utf-8') as spring:
        json.dump(courses, spring, indent=4, ensure_ascii=False)
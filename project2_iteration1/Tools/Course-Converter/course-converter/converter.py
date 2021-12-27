import pandas, sys, json

def convertJSON(filePath: str) -> list:

    excelFile = pandas.read_excel(filePath).iloc()

    courses = []

    for row in excelFile:
        courses.append(
            {
                'courseCode': row[0],
                'courseName': row[1],
                'lecturer': row[2],
                'credit': float(row[5]),
                'quota': int(row[7]),
                'schedule': parseTime(str(row[8])),
                'prerequisite': ''
            }
        )

    return courses

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

    courses = convertJSON(filePath)

    writeFile(filePath.rsplit('.', maxsplit=1)[0] + '.json', courses)
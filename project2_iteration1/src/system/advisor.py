from course.course_registration import CourseRegistration, RegistrationStatus
from course.course_section import CourseSection

class Advisor:

    def __init__(self, name, surname):
        self.name = name
        self.surname = surname
        self.students = []

    @property
    def fullName(self) -> str:
        return self.name + ' ' + self.surname
    
    def checkRegistration(self, courseRegistration: CourseRegistration):

        teCount = 0
        courseSizes = len(courseRegistration.courses)
        i = 0
        while i < courseSizes: 
            currentCourse = courseRegistration.courses[i]
            if currentCourse.group == "TE": teCount += 1

            if  teCount > 2 and courseRegistration.student.transcript.currentSemester % 2 == 1 and currentCourse.group == "TE":
                print('te count')
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue

            if (currentCourse.group == "FTE" and courseRegistration.student.transcript.currentSemester % 2 == 1 and \
                courseRegistration.student.transcript.completedCredits != 235):

                print('fte count')                
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue

            requiredCredit = currentCourse.requieredCredits
            if courseRegistration.student.transcript.completedCredits < requiredCredit:
                
                print('req credit')
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue

            i += 1

        self.collisionCheck(courseRegistration)

        courseRegistration.completeRegistration()

    def getScheduleTimes(self, schedule):
        times = []

        for s in schedule:
            
            courseTimes = [0, 0]
            splitedTime = s.split("-", 3)

            splitedStartTime = splitedTime[1].split(":", 2)
            splitedEndTime = splitedTime[2].split(":", 2)

            day = int(splitedTime[0])
            courseTimes[0] = day * 24 * 60 + int(splitedStartTime[0]) * 60 + int(splitedStartTime[1])
            courseTimes[1] = day * 24 * 60 + int(splitedEndTime[0]) * 60 + int(splitedEndTime[1])

            times.append(courseTimes)

        return times

    def getTotalCollisionMinutes(self, firstSchedule, secondSchedule):
        firstTimes = self.getScheduleTimes(firstSchedule)
        secondTimes = self.getScheduleTimes(secondSchedule)

        totalCollisionMinute = 0

        for time1 in firstTimes:
            for time2 in secondTimes:
                # | ......1.............2..........................2............1........ |
                if time1[0] <= time2[0] and time1[1] >= time2[1]:
                    totalCollisionMinute += time1[1] - time1[0]

                # | ......1.............2..........................1............2........ |
                elif time1[0] <= time2[0] and time1[1] >= time2[0] and time1[1] <= time2[1]:
                    totalCollisionMinute += time1[1] - time2[0]

                # | ......2.............1..........................1............2........ |
                elif time1[0] >= time2[0] and time1[0] <= time2[1]:
                    totalCollisionMinute += time1[1] - time1[0]

                # | ......2.............1..........................2............1........ |
                elif time1[0] >= time2[0] and time1[0] <= time2[1] and time1[1] >= time2[1]:
                    totalCollisionMinute += time2[1] - time1[0]

        return totalCollisionMinute

    def collisionCheck(self, courseRegistration: CourseRegistration):

        courseSizes = len(courseRegistration.courses)
        i = 0
        while i < courseSizes:
            j = i + 1
            while j < courseSizes:
                totalCollisionMinute = self.getTotalCollisionMinutes(courseRegistration.studentSchedule[i][0].schedule,
                        courseRegistration.studentSchedule[j][0].schedule)

                if (totalCollisionMinute > 59):
                    print('collision')
                    # TODO : log here
                    courseRegistration.courses.pop(j)
                    courseSizes -= 1
                j += 1
            i += 1
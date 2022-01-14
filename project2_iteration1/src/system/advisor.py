from course.course_registration import CourseRegistration, RegistrationStatus
from src.course.course_section import CourseSection

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
                
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue

            if (currentCourse.group == "FTE" and courseRegistration.student.transcript.currentSemester % 2 == 1 and \
                courseRegistration.student.transcript.completedCredits != 235):
                
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue

            requiredCredit = currentCourse.requieredCredits
            if courseRegistration.student.transcript.completedCredits < requiredCredit:
                
                # TODO : log here
                courseRegistration.courses.pop(i)
                courseSizes -= 1
                continue
            
            for section, labSection in courseRegistration.studentSchedule:
                section.addStudent(courseRegistration.student)

            i += 1

        self.collisionCheck(courseRegistration)
        
        for section, labSection in courseRegistration.studentSchedule:
            section.addStudent(courseRegistration.student)
            if labSection != None:
                labSection.addStudent(courseRegistration.student)

        courseRegistration.status = RegistrationStatus.COMPLETE
        courseRegistration.completeRegistration()

    def getScheduleTimes(schedule):
        times = []

        for s in schedule:
            
            courseTimes = [0, 0]
            splitedTime = s.split("-", 3)

            splitedStartTime = splitedTime[1].split("\\.", 2)
            splitedEndTime = splitedTime[2].split("\\.", 2)

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
        # courseRegistration.courses = [Course]
        # courseRegistration.studentSchedule = [(CourseSection, None or CourseSection)]

        # If there is collision remove tuple from the studentSchedule and the respective course from the courses and
        # add section that causes to the collision to the courseRegistration.blacklist (only that course section)
        # if collision is less than 60 minute ignore.

        courseSizes = len(courseRegistration.courses)
        i = 0
        while i < courseSizes:

            # Attending the course is not mandatory when taking the course again if student didn't fail the course with DZ.
            #if student.getCourseNote(courseRegistration.courses[i]) >= 0: continue
            
            j = 0
            while j < courseSizes:
                totalCollisionMinute = self.getTotalCollisionMinutes(courseRegistration.courses[i].sections[0],
                        courseRegistration.courses[j].sections[0])

                # if there is any collision remove the course
                if (totalCollisionMinute > 59):
                    # TODO : log here

                    # ??????????????????????????????????????????
                    #currentCourses.get(j).removeStudent(student)
                    # ??????????????????????????????????????????
                    courseRegistration.courses.pop(j)
                    courseSizes -= 1
                j += 1
        i += 1
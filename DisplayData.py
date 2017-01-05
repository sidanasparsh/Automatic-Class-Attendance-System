from pymongo import MongoClient
from flask import Flask, render_template,jsonify
import json
import requests, datetime
from time import gmtime, strftime
from collections import OrderedDict
from datetime import timedelta

app= Flask(__name__)

client = MongoClient('mongodb://rasp:berry@ds111178.mlab.com:11178/attendencesystem')
db = client.attendencesystem

@app.route('/')
def hello():
    return 'hello'

@app.route('/<string:courseid>')
def totalAttendance(courseid):
    """Total attendance in a given course over a period of time. Will be displayed as a line graph"""
    dict={}
    dateTime=strftime("%Y-%m-%d %H:%M:%S")
    date,time=dateTime.split(" ")
    totalAttendance = db.Attendance.find()
    for ta in totalAttendance:
        if ta["Course"] == courseid and ta["IsPresent"] == "No":
            if str(ta["Date"]) not in dict:
                dict[str(ta["Date"])] = 1
            else:
                dict[str(ta["Date"])]+=1
    dict = OrderedDict(sorted(dict.items(),key=lambda kv:kv[0]))
    print dict.keys()
    print dict.values()
    print type(courseid)
    course = str(courseid)
    print type(course)
    return render_template("index.html", date = dict.keys(), attendance = dict.values(), course = course)

@app.route('/search/<int:sjsuid>/<string:course>')
def specificAttendance(sjsuid,course):
    '''Individual attendance of a student, Will be displaying pie chart of the attendance'''
    Attendance = db.Attendance.find({"SJSUID" :sjsuid, "Course": course})
    total_classes = 0 #in a specific course
    classes_attended = 0
    sObj= db.Students.find_one({"_id":sjsuid})
    sname = str(sObj['Name'])+"("+str(sObj['_id'])+")"

    for ta in Attendance:
        total_classes +=1
        if ta["IsPresent"] == "Yes": #Else it will be NO -- Point being his entry will always be there
            classes_attended +=1

    return render_template("specificAttendance.html", student_name=sname, total_classes = total_classes, classes_attended = classes_attended, course=course)


@app.route('/compsearch/<string:idlist>/<string:course>')
def compAttendance (idlist,course):
    student_list = idlist.split(";")
    for i in range(0,len(student_list)):
        student_list[i] = int(student_list[i])
    print student_list
    dict={}


    totalAttendance = db.Attendance.find({"Course": course, "IsPresent":"Yes"})

    student_namelist=db.Students.find()

    name = {}
    for s in student_namelist:
        #print s['_id']
        if s['_id'] in student_list:
            print "Got inside"
            name[s['_id']] = str(s['Name'])

    print name

    for s in student_list:
        dict[name[s]+"("+str(s)+")"] = 0

    print dict

    print student_list
    for s in student_list:
        print "I must be executed three times"
        for ta in totalAttendance:
            print "comparing",s,"&", ta["SJSUID"]
            if int(ta["SJSUID"]) == s:
                dict[name[s]+"("+str(s)+")"] +=1
        totalAttendance = db.Attendance.find({"Course": course, "IsPresent": "Yes"})

    list=[]
    print "**************************"
    print dict
    for i in range(0, len(dict.keys())):
        list.append({'name': dict.keys()[i], 'y':dict.values()[i]})

    # for ta in totalAttendance:
    #     if str(ta["SJSUID"]) not in student_list:
    #         dict[str(ta["SJSUID"])] = 1
    #     else:
    #         dict[str(ta["SJSUID"])] += 1
    #list= json.dumps(list)
    print list
    return render_template("compAttendance.html", course=course, complist =list)

@app.route('/livestats/<string:course_id>')
def getTotalAttendence(course_id):
    print "livestats"
    listOfPresentStudents=[]
    listOfAbsentStudents=[]
    #date = str(datetime.datetime.now() + timedelta(days=1)).split(" ")[0]
    datet = str(datetime.datetime.now())
    date,time = datet.split(" ")
    print date
    totalStudentsPresent=db.Attendance.find({"IsPresent":"Yes", "Date":date, "Course":course_id})
    totalStudentsAbsent = db.Attendance.find({"IsPresent": "No", "Date":date, "Course":course_id})
    #totalStudentsAbsent = db.Attendance.find({"IsPresent": "No"})
    for i in totalStudentsPresent:
        #listOfPresentStudents.append(i["SJSUID"])
        currentStudent=db.Students.find_one({'_id':i['SJSUID']})
        listOfPresentStudents.append(currentStudent["Name"])
    for i in totalStudentsAbsent:
        #listOfAbsentStudents.append(i["SJSUID"])
        currentStudent = db.Students.find_one({'_id': i['SJSUID']})
        listOfAbsentStudents.append(currentStudent["Name"])
    print listOfAbsentStudents
    print listOfPresentStudents
    return render_template("livestats.html", listOfPresentStudents=listOfPresentStudents, listOfAbsentStudents=listOfAbsentStudents)


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8008, debug=True)
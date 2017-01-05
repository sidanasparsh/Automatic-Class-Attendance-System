from pymongo import MongoClient
from flask import Flask,url_for , redirect, session ,flash,request, jsonify
from flask import request
#import datetime
from datetime import datetime
import json
from pytz import timezone
import pytz
from time import gmtime, strftime

app= Flask(__name__)
#con = MongoClient()
#db = con.StudentAttendenceManagement

con=MongoClient('mongodb://rasp:berry@ds111178.mlab.com:11178/attendencesystem')
db=con.attendencesystem

@app.route('/')
def hello():
    return "hello"

@app.route('/v1/students/<string:fullname>/<string:password>/<string:courses>/<int:sjsuID>/<string:imei>', methods=['POST'])
def registerStudent(fullname, password, courses, sjsuID, imei):
    try:
        dateTime = strftime("%Y-%m-%d %H:%M:%S")
        date, time = dateTime.split(" ")

        listOfCourses=courses
        print 'courses are', (listOfCourses)
        print 'fullname is ', fullname
        print 'password is ', password
        print 'courses is' ,courses
        print 'id is', sjsuID
        print 'IMEI is', imei
        db.Students.insert({"_id":sjsuID,
                           "Name": fullname,
                           "Password": password,
                           "Courses":listOfCourses,
                           "AndroidID":imei
                           })
        db.Attendance.insert({"IsPresent":"No","Course":listOfCourses,"CheckOutTime":None,"CheckInTime":None,"SJSUID":sjsuID,"Date": date})
    except:
        return jsonify([]), 208
           #return "Record Already Exists", 400
    #return jsonify([{'fullname': fullname, 'coursename':listOfCourses, 'sjsuid':sjsuID,
    #            'androidID': imei}]), 200
    return jsonify([]), 200
        #return jsonify([]), 200



'''
    return json.dumps({'Name': fullname, 'password': password, 'email': expense.email, 'course1': course1,
                       'course2': course2, 'course3': course3,
                       'estimated_costs': expense.estimated_costs, 'submit_date': expense.submit_date,
                       'course4': course4, 'androidID': androidID}), 200
                       '''

@app.route('/v1/attendence/checkOUT/', methods=['PUT'])
def checkout():
  value = request.get_json(force=True)
  token = value["token"]
  imei = value["imei"]
  print token
  print imei
  try:
    currentToken=db.Tokens.find_one({"Token":token})
  except:
    return "WTF", 200  
  dateTime=strftime("%Y-%m-%d %H:%M:%S")
  date,time=dateTime.split(" ")
  student = db.Students.find_one({"AndroidID": imei})
  sjsu_id = student['_id']
  studentRecord=db.Attendance.find_one({"SJSUID": sjsu_id, "Date":date,"IsPresent": "Yes"})
  if studentRecord["CheckInTime"] is not None and studentRecord["CheckOutTime"] is not None:
        return "Your attendance has been marked."
  if studentRecord["CheckOutTime"] is None:
    studentRecord["CheckOutTime"] = time
    print " You have been checked OUT"
    db.Attendance.save(studentRecord)
  return json.dumps({"Name": student["Name"], "Present": studentRecord["IsPresent"]}), 200




    


@app.route('/v1/attendence/checkIN/', methods=['PUT'])
def markAttendence():
    value = request.get_json(force=True)
    token = value["token"]
    imei = value["imei"]
    print token
    print imei
    try:
        currentToken=db.Tokens.find_one({"Token":token})
        print "1..."
        print currentToken["Course"]
        print "2...."
    except:
        return "Invalid Token", 400
    dateTime=strftime("%Y-%m-%d %H:%M:%S")
    date,time=dateTime.split(" ")
    print date
    print time
    print id
    print currentToken["Course"]
    student = db.Students.find_one({"AndroidID": imei})
    sjsu_id = student['_id']
    print sjsu_id

    studentRecord=db.Attendance.find_one({"SJSUID": sjsu_id, "Date":date,"Course": currentToken["Course"]})
    if studentRecord["CheckInTime"] is not None and studentRecord["CheckOutTime"] is not None:
        return "Your attendence has been marked."
    if studentRecord['CheckInTime'] is None:
      studentRecord["CheckInTime"] = time
       # db.Attendance.update({"AndroidID":imei, "Date":date,"Course": currentToken["Course"]},{"$set": {"IsPresent":"Present", "CheckInTime": studentRecord["CheckInTime"]}})
      studentRecord["IsPresent"]="Yes"
      db.Attendance.save(studentRecord)
      print " Checked in"
   #  elif studentRecord["CheckOutTime"] is None:
   #    studentRecord["CheckOutTime"] = time
   #      #db.Attendance.update({"AndroidID":imei, "Date":date,"Course": currentToken["Course"]},{"$set": {"IsPresent":"Present", "CheckOutTime": studentRecord["CheckOutTime"]}})
   #    print "Checked out"
   #    db.Attendance.save(studentRecord)
   # # studentRecord["Timestamp"]=datetime.datetime.utcnow()
    return json.dumps({"Name": student["Name"], "Present": studentRecord["IsPresent"]}), 200

# @app.route('/v1/students/<int:student_id>/<string:password>', methods=['GET'])
# def login(student_id, password):
#     currentStudent=db.Students.find_one({"_id":student_id})
#     if currentStudent is not None:
#         if currentStudent['Password']== password:
#             return jsonify([{'fullname': currentStudent['Name'], 'coursename': currentStudent['Courses'], 'sjsuid': currentStudent['_id'],
#                              'androidID': currentStudent['AndroidID']}]), 200
#         return jsonify([{'Error':"Invalid password"}]),404
#     return jsonify([{'Error': "Invalid username"}]),404



@app.route("/v1/students/courses/<string:imei>", methods=['GET'])
def getCourses(imei):
    currentStudent=db.Students.find_one({ "AndroidID":imei})
    print currentStudent['Name']
    print currentStudent['Courses']
    listOfCourses = []
    listOfCourses.append(currentStudent['Courses'])
    courses=""
    for i in listOfCourses:
        courses=courses+i+";"
    #courses=listOfCourses[0]
    courses=courses[:-1]
    print courses
    return json.dumps({"Courses":courses}), 200

@app.route('/v1/students/attendance/<string:imei>/<string:course_id>', methods=['GET'])
def getAttendence(imei, course_id):
    print imei
    print course_id
    dic = {}
    student = db.Students.find_one({"AndroidID": imei})
    entireHistory = db.Attendance.find({"Course": course_id, "SJSUID": student['_id']})
    counter = 1
    for i in entireHistory:
        print "Inside Loop"
        if i["CheckInTime"] is not None and i["CheckOutTime"] is not None:
            dic[counter] = {"Date": i["Date"], 'Marked': i["IsPresent"], 'CheckInTime': i['CheckInTime'], 'CheckOutTime': i['CheckOutTime']}
            counter = counter + 1
    return json.dumps(dic), 200

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5004, debug=True)

'''
db.Students.insert({"_id": 01141242,
                    "Name": "Abc Xyz",
                    "password": "Passowrd",
                    "Courses":["272","273"],
                    "androidID": "12898ew"
                    })
db.Attendence.insert({"SJSUID":["_id"],
                      "course":course,
                      "IsPresent": 0,
                      "Date": datetime.date.utcnow(),
                      "Timestamp":datetime.datetime.utcnow()})
db.tokens.insert({"Date": datetime.date.utcnow(),
                  "Course": course,
                  "Token": "INSERT TOKEN VARIABLE"})

'''

'''
@app.route('/history', methods=['GET'])
def getHistory(date, course):
    count=0
    dateRecord=db.Attendence.find({"Date":date, "Course":course})
    for i in dateRecord:
        if i["IsPresent"]==1:
            count=count+1
    print("Total Attendence is :"+str(count))
'''


'''
    db.Attendence.update({"_id":id, "date":datetime.date.today(), "Course":currentToken["Course"]},
                          {"IsPresent":1,
                          "Timestamp":datetime.datetime.utcnow()
                          })

    db.Attendence.find_one({"_id":id, date })
    return json.dumps({'token': currentToken[""], '_id': id, 'IsPresent': expense.email, 'course1': course1,
                       'course2': course2, 'course3': course3,
                       'estimated_costs': expense.estimated_costs, 'submit_date': expense.submit_date,
                       'course4': course4, 'androidID': androidID}), 200
'''

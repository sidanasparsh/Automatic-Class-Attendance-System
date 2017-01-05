from pymongo import MongoClient
import datetime, requests

con = MongoClient('mongodb://rasp:berry@ds111178.mlab.com:11178/attendencesystem')
db = con.attendencesystem

# db.Students.insert([
#     {"_id": 1784681,
#     "Courses": "CMPE202",
#     "Name": "alfred",
#     "Password": "wewe",
#     "AndroidID": "867634029192167"
#     },
#     {
#     "_id": 11235371,
#     "Courses": "CMPE202",
#     "Name": "roman",
#     "Password": "yomama",
#     "AndroidID": "867634029192267"
#     },
#     {
#     "_id": 1176325,
#     "Courses": "CMPE272",
#     "Name": "superman",
#     "Password": "yomama",
#     "AndroidID": "867634029192367"
#     },
#     {
#     "_id": 1436325,
#     "Courses": "CMPE272",
#     "Name": "spiderman",
#     "Password": "yomama",
#     "AndroidID": "867634029192467"
#     },
#     {
#     "_id": 1446325,
#     "Courses": "CMPE272",
#     "Name": "batman",
#     "Password": "yomama",
#     "AndroidID": "867634029192567"
#     },
#     {
#     "_id": 1456325,
#     "Courses": "CMPE272",
#     "Name": "flash",
#     "Password": "yomama",
#     "AndroidID": "867634029192667"
#     }
# ])
# print "Students added to database"

db.Attendance.insert([
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-27",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-28",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-30",
    "CheckInTime": None,
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-01",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-02",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-12-03",
    "CheckInTime": None,
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-04",
    "CheckInTime": "00:57:32",
    "SJSUID": 11413404
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-12-05",
    "CheckInTime": None,
    "SJSUID": 11413404
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-27",
    "CheckInTime": "00:57:32",
    "SJSUID": 12335
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-28",
    "CheckInTime": None,
    "SJSUID": 12335
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 12335
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-30",
    "CheckInTime": None,
    "SJSUID": 12335
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-01",
    "CheckInTime": "00:57:32",
    "SJSUID": 12335
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-02",
    "CheckInTime": "00:57:32",
    "SJSUID": 12335
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-12-03",
    "CheckInTime": None,
    "SJSUID": 12335
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-12-04",
    "CheckInTime": None,
    "SJSUID": 12335
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-05",
    "CheckInTime": "00:57:32",
    "SJSUID": 12335
},
{
    "IsPresent": "No",
    "Course": "CMPE202",
    "CheckOutTime": None,
    "Date": "2016-11-27",
    "CheckInTime": None,
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-28",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-30",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-01",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "No",
    "Course": "CMPE202",
    "CheckOutTime": None,
    "Date": "2016-12-02",
    "CheckInTime": None,
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-03",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-04",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE202",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-05",
    "CheckInTime": "00:57:32",
    "SJSUID": 114123
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-27",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-28",
    "CheckInTime": None,
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-30",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-01",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-02",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-03",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-04",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-05",
    "CheckInTime": "00:57:32",
    "SJSUID": 1467711
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-24",
    "CheckInTime": None,
    "SJSUID": 11224371
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-25",
    "CheckInTime": None,
    "SJSUID": 11224371
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-27",
    "CheckInTime": None,
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-28",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-30",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-12-01",
    "CheckInTime": None,
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-02",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-03",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-04",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-05",
    "CheckInTime": "00:57:32",
    "SJSUID": 11224371
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-27",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "No",
    "Course": "CMPE273",
    "CheckOutTime": None,
    "Date": "2016-11-28",
    "CheckInTime": None,
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-29",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-11-30",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-01",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-02",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-03",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-04",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
},
{
    "IsPresent": "Yes",
    "Course": "CMPE273",
    "CheckOutTime": "00:59:10",
    "Date": "2016-12-05",
    "CheckInTime": "00:57:32",
    "SJSUID": 114421
}
])

print "Attendance marked for students"
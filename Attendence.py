from pymongo import MongoClient
#from flask import Flask,url_for , redirect, session ,flash
import datetime, requests
from random import randint
from flask import request, Response
from flask import render_template
#from flask_oauth import OAuth

#app= Flask(__name__)
#con = MongoClient()
con = MongoClient('mongodb://rasp:berry@ds111178.mlab.com:11178/attendencesystem')
#db = con.AttendanceTracker
db = con.attendencesystem
#@app.route('/')
#def post_to_database():
rand = randint(10**9,10**10)
print "new random token is",rand,"at",datetime.datetime.now().time()
sclass= db.Tokens.find_one({"Course":"CMPE273"})
sclass["Token"] = rand
db.Tokens.save(sclass)


#return 'Posted data in Sithu\'s class'


#if __name__ == '__main__':
#    app.run(debug=True, host='0.0.0.0', port=8082)

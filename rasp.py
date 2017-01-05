from pymongo import MongoClient
import requests

client = MongoClient('mongodb://rasp:berry@ds111178.mlab.com:11178/attendencesystem')

db = client.attendencesystem

token = 0
#db.Tokens.insert({"Course":"CMPE278", "Token":1234})

classes = db.Tokens.find({"Course":'CMPE273'})
for c in classes:
    if c["Course"] == 'CMPE273':
        token = c["Token"]

print token
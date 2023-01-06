from firebase_admin import credentials
import json
import firebase_admin
from firebase_admin import firestore


cred = credentials.Certificate(
    "harry-potter-memory-firebase-adminsdk-3ca73-c28ac1bbdf.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

f = open('list.json')
data = json.load(f)

for i in data:
    doc_ref = db.collection(u'resimler').document(i["name"])
    doc_ref.set(i)


f.close()

from time import mktime
from google.appengine.ext import db
#from google.appengine.api import users

class Vote(db.Model):
    score = db.IntegerProperty(required=True, default=0,choices=(1, -1, 100500))
    created_at = db.DateTimeProperty(auto_now_add=True)

    def get_created_at_tm(self):
        return int(self.created_at.strftime("%s")) * 1000

    def to_obj_json(self):
        return {'score':self.score,
                 'created_at': self.get_created_at_tm()}

    def to_list_json(self):
        return [self.get_created_at_tm(), self.score]

class Event(db.Model):
    started_at = db.DateTimeProperty(auto_now_add=True)
#!/usr/bin/env python
from datetime import datetime

from google.appengine.ext import webapp
from google.appengine.ext.webapp import util, template
from django.utils import simplejson
from google.appengine.api import users

from model import Vote, Event

vote_map = {"like":1,
            "dont-like":-1,
            "mega":100500}

class VoteHandler(webapp.RequestHandler):
    def get(self, vote):
        if vote in vote_map:
            Vote(score=vote_map[vote]).save()
        self.response.out.write('OK!')

class VoteAdminHandler(webapp.RequestHandler):
    def to_json(self, votes):
        return simplejson.dumps([vote.to_list_json() for vote in votes])

    def get(self):
        votes = Vote.all().fetch(100)
        self.response.out.write(self.to_json(votes))

class VoteCountHandler(webapp.RequestHandler):
    def get(self, starting_from):
        votes = Vote.all()
        if starting_from:
            votes.filter("created_at > ", datetime.fromtimestamp(int(starting_from)/1000))
        count = votes.count(limit=None)
        self.response.out.write(count)

class EventMarkingHandler(webapp.RequestHandler):
    def get(self):
        Event().save()

def main():
    application = webapp.WSGIApplication([
            (r'/do/logout', webapp.RedirectHandler.new_factory(users.create_logout_url('/index.html'))),
            (r'/do/login', webapp.RedirectHandler.new_factory(users.create_login_url('/_ah/admin'))),

            (r'/do/vote/num/from/(.*)', VoteCountHandler),
            (r'/do/vote/(.*)', VoteHandler),

            (r'/do/events/mark', EventMarkingHandler),

            (r'/do/admin/votes/list', VoteAdminHandler),
            (r'/admin/', webapp.RedirectHandler.new_factory('/admin/index.html', permanent=True)),
            (r'.*', webapp.RedirectHandler.new_factory('/like.html', permanent=True))],
         debug=True)
    util.run_wsgi_app(application)


if __name__ == '__main__':
    main()

from bottle import request
from datetime import datetime
from datetime import date

def get_user_input():
    '''Hämtar användarinput från HTML-formuläret, kan snyggas till genom att lägga in värden i dictionary istället för lista'''
    user_input = []
    '''Kolla hur man lägger till namn + value som dictionaries, splitta location!!! bara första ordet!!!'''
    location = getattr(request.forms,"destination")
    user_input.append(location)
    print(location)
    start = getattr(request.forms, "from")
    user_input.append(start)
    print(start)
    finish = getattr(request.forms, "to")
    user_input.append(finish)
    print(finish)
    transport = request.forms.getall("transport")
    user_input.append(transport)
    print(transport)
    accommodation = request.forms.getall("accommodation")
    user_input.append(accommodation)
    print(accommodation)
    activity = request.forms.getall("activities")
    user_input.append(activity)
    print(activity)
    

    return user_input

def get_length(user_input):
    '''Lägg till i pythonanywhere. Räknar ut resans längd i dagar utifrån användarens datum inmatning, om större än 14 utgår vi ifrån att man tvättar!!!'''
    start = user_input[1]
    finish = user_input[2]

    dt_start = datetime.strptime(start, "%Y/%m/%d")
    dt_finish = datetime.strptime(finish, "%Y/%m/%d")
    date_start = dt_start.date()
    date_finish = dt_finish.date()
    length = date_finish - date_start

    return length.days

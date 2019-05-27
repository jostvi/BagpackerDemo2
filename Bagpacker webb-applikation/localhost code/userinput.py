from bottle import request
from datetime import datetime
from datetime import date

def get_user_input():
    '''Gets user input from html-form'''
    user_input = []
    
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
    '''Calculates length of trip in days based on start and finish dates'''
    start = user_input[1]
    finish = user_input[2]

    dt_start = datetime.strptime(start, "%Y/%m/%d")
    dt_finish = datetime.strptime(finish, "%Y/%m/%d")
    date_start = dt_start.date()
    date_finish = dt_finish.date()
    length = date_finish - date_start

    return length.days

'''
    File name: bagpacker_demo_v.3.py
    Author: Christina Knepper
    Python Version: 3.7.1
'''

from bottle import default_app, route, run, template, static_file, request, redirect, error, post
from geopy.geocoders import Nominatim
import random
import requests, json
import psycopg2
import psycopg2.extras
import userinput as ui
import weather as w
import generatelist as gl
import database as db
from datetime import datetime
from datetime import date

        
@route("/")
def show_startpage():
    '''Returns the startpage'''
    return template ("index")

@route("/newlist")
def show_form():
    '''Returns the form for collecting user input'''
    
    return template ("javascriptform")

@route("/validate_destination", method="POST")
def validate_destination():
    '''Direct validation of destination'''
    destination = getattr(request.forms, "destination")
    print(destination)
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(destination)

    try:
        lat = geolocation.latitude
        print(lat)
        message = {"valid" : True}
        return json.dumps(message)

    except:
        message = {"valid" : False, "message" : "Destinationen du har angett är inte giltig."}
        return json.dumps(message)
    

@route("/generate_list", method="POST")
def generate_list():
    '''Generates packing list based on user input'''
    user_input = ui.get_user_input()
    returns = gl.create_item_list(user_input)
    item_list = returns[0]
    location_raw = returns[1]
    location_list = location_raw.split(",")
    location = location_list[0], location_list[-1]
    temp_min = returns[2]
    temp_max = returns[3]
    length = returns[4]
    which_data = returns[5]
    total_weight = returns[6]
    rain = returns[7]
    if rain == True:
        rain_risk = "hög"
    else:
        rain_risk = "låg"
    mean_temp = returns[8]

    return template ('show_list', item_list = item_list, length = length, which_data = which_data,
                     location = location, temp_min = round(temp_min), temp_max = round(temp_max),
                     total_weight = total_weight, rain_risk = rain_risk, start = user_input[1],
                     finish = user_input[2], mean_temp = round(mean_temp))

@route("/android/")
def generate_list_android():
    '''Generates packing list based on input sent via HTTP request from Android'''

    user_input = []
    destination = request.query.param1
    user_input.append(destination)
    
    start = request.query.param2
    user_input.append(start)
    
    finish = request.query.param3
    user_input.append(finish)
    
    transport = request.query.param4
    transport_list = transport.split(",")
    user_input.append(transport_list)

    accommodation = request.query.param5
    accommodation_list = accommodation.split(",")
    user_input.append(accommodation_list)

    activity = request.query.param6
    activity_list = activity.split(",")
    user_input.append(activity_list)

    result = create_item_list(user_input)
    complete_list = result[0]
    
    destination = result[1]
    temp_min = result[2]
    temp_max = result[3]
    lenght = result[4]
    weather_data = result[5]

    the_list = {"lista" : complete_list, "destination" : destination, "temp_min" : temp_min,
                "temp_max" : temp_max, "length" : length, "weather_data" : weather_data}
    return json.dumps(the_list)
 

@route("/get_saved_list", method="POST")
def get_saved_list():
    '''Returns saved list'''
    list_id = getattr(request.forms, "code")
    
    conn = db.db_connection()
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, saved_lists.quantity, checked FROM saved_lists JOIN all_items on item_id=id WHERE code = (%s)",
                (list_id, ))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    complete_list = []
    for item in item_list:
        item_dict = {"id": item[0], "item": item[1], "weight": item[2], "category": item[3],
                     "quantity": item[4], "checked" : item[5]}
        complete_list.append(item_dict)


    conn = db.db_connection()
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT destination, start, finish, temp_min, temp_max, temp_mean, which_data, rain from weather_data WHERE id = (%s)",
                (list_id, ))
    data = cur.fetchall()
    cur.close()
    conn.close()
    weather_data = data[0]
    destination = weather_data[0]
    location = destination.split(",")
    start = weather_data [1]
    finish = weather_data[2]
    temp_min = weather_data[3]
    temp_max = weather_data[4]
    mean_temp = weather_data[5]
    which_data = weather_data[6]
    rain = weather_data[7]
    if rain == True:
        rain_risk = "hög"
    else:
        rain_risk = "låg"
    
    
    total_weight = gl.get_weight(complete_list)

    return template ('show_saved_list', item_list = item_list, which_data = which_data,
                     start = start, finish = finish, location = location,
                     temp_min = round(temp_min), temp_max = round(temp_max),
                     total_weight = total_weight, rain_risk = rain_risk,
                     mean_temp = round(mean_temp))

@route("/save_list/", method="POST")
def save_list():
    '''Saves list and returns randomized code which can be used to retrieve the list''' 
    item_list_to_save = getattr(request.forms, "saved_list")
    item_list = json.loads(item_list_to_save.replace("\'", "\""))
    
    complete_list = []
    for item in item_list:
        checked = getattr(request.forms, str(item['id']))
        if checked == "true":
            item['checked'] = True
        else:
            item['checked'] = False
        complete_list.append(item)
    
    code = random.randint(100000,1000000)
    conn = db.db_connection()
    cur = conn.cursor()
    for item in complete_list:
        cur.execute('INSERT into saved_lists VALUES (%s, %s, %s, %s)',
                    (code, item['id'], item['quantity'], item['checked']))
    conn.commit()
    cur.close()
    conn.close()

    destination = getattr(request.forms, "destination")
    start = getattr(request.forms, "start")
    finish = getattr(request.forms, "finish")
    temp_min = int(getattr(request.forms, "temp_min"))
    temp_max = int(getattr(request.forms, "temp_max"))
    temp_mean = int(getattr(request.forms, "temp_mean"))
    which_data = getattr(request.forms, "which_data")
    rain_risk = getattr(request.forms, "rain_risk")
    
    if rain_risk == "hög":
        rain = True
    else:
        rain = False

    conn = db.db_connection()
    cur = conn.cursor()
    cur.execute('INSERT into weather_data VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)',
                (code, destination, start, finish, temp_min, temp_max, temp_mean, which_data, rain))
    conn.commit()
    cur.close()
    conn.close()
    return template ('code', code=code)

@route("/edit_list/", method="POST")
def edit_list():
    item_list_to_edit = getattr(request.forms, "saved_list")
    item_list = json.loads(item_list_to_edit.replace("\'", "\""))
    
    
    return template('edit_list', item_list = item_list)


@route("/fetch_list")
def fetch_list():

    return template ('get_list')

@route("/packhacks")
def show_packhacks():

    return template ('packhacks')

@route("/about")
def show_about():

    return template ('about')

@route("/static/<filename>")
def static_files(filename):
    return static_file (filename, root="static")


@error(404)
def error404(error):
   return template ("error")

@error(405)
def error405(error):
   return template ("error")

@error(500)
def error500(error):
   return template ("error")

run(host="localhost", port=8080, debug=True)

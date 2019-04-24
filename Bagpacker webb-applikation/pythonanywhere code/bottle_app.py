'''
    File name: bagpacker_demo_v.2.py
    Author: Christina Knepper
    Python Version: 3.7
'''
 # -*- coding: utf-8 -*-
from bottle import default_app, route, run, template, static_file, request, redirect, error
from geopy.geocoders import Nominatim
from pyowm import OWM
from statistics import mean
import requests, json
import pymysql
import pymysql.cursors
import cloudpickle
import random
from datetime import datetime
from datetime import date

def get_user_input():
    '''Hämtar användarinput från HTML-formuläret'''
    user_input = []
    '''Kolla hur man lägger till namn + value som dictionaries'''
    location = getattr(request.forms,"location")
    user_input.append(location)
    start = getattr(request.forms, "from")
    user_input.append(start)
    finish = getattr(request.forms, "to")
    user_input.append(finish)
    transport = getattr(request.forms, "transport")
    user_input.append(transport)
    accommodation = getattr(request.forms, "accommodation")
    user_input.append(accommodation)
    activity = getattr(request.forms, "activity")
    user_input.append(activity)

    return user_input

def get_length(user_input):
    '''Räknar ut resans längd i dagar utifrån användarens datum inmatning, om större än 14 utgår vi ifrån att man tvättar!!!'''
    start = user_input[1]
    finish = user_input[2]

    dt_start = datetime.strptime(start, "%Y/%m/%d")
    dt_finish = datetime.strptime(finish, "%Y/%m/%d")
    date_start = dt_start.date()
    date_finish = dt_finish.date()
    length = date_finish - date_start

    return length.days

def current_or_historic_weather_data(user_input):
    '''Avgör om resan påbörjas inom de närmsta fem dagarna,
    måste även vara beroende av resans längd!!!, ev. kombination av KG och aktuell prognos i så fall?'''
    start = user_input[1]
    dt_start = datetime.strptime(start, "%Y/%m/%d")
    date_start = dt_start.date()
    today = date.today()

    print(date_start)
    print(today)
    difference = date_start - today
    print(difference.days)
    r = range(0,6)
    if difference.days in r:
        return "current"
    else:
        return "historic"

def get_season(user_input):
    '''Avgör om resan görs sommar eller vintertid, OBS! hur hanterar vi gränsfall?'''
    start = user_input[1]
    finish = user_input[2]

    dt1 = datetime.strptime(start, "%Y/%m/%d")
    dt2 = datetime.strptime(finish, "%Y/%m/%d")
    month1 = dt1.month
    month2 = dt2.month
    print(month1)
    print(month2)
    month = month1
    r = range(4,9)
    if month in r:
        return "summer"
    else:
        return "winter"


def get_historic_weather_data(zone, season):
    '''Hämtar historisk väderdata baserad på klimatzon och årstid'''
    zone = zone
    season = season
    if season == "winter":
        db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute("SELECT min_temp_winter, max_temp_winter, rain_winter from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        db.close()
        return forecast[0]

    else:
        db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute("SELECT min_temp_summer, max_temp_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        db.close()
        return forecast[0]


def get_general_items():
    '''Hämtar items som inte är beroende av några parameter'''
    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker', cursorclass=pymysql.cursors.DictCursor)
    cur = db.cursor()
    cur.execute("SELECT item, weight, category from all_items where general = true")
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_activity_items(user_input):

    activity = user_input

    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker', cursorclass=pymysql.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN activities ON id=item_id WHERE activity = %s', (activity, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_accommodation_items(user_input):

    accommodation = user_input

    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker', cursorclass=pymysql.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN accommodation ON id=item_id WHERE type = %s', (accommodation, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_transport_items(user_input):

    transport = user_input

    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker', cursorclass=pymysql.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN transport ON id=item_id WHERE type = %s', (transport, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_geolocation(user_input):
    '''Gör om användarinput till geolocation'''
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input)

    return geolocation.latitude, geolocation.longitude

def get_weather_forecast(geolocation):
    '''Hämtar max och min temperatur samt information om nederbörd
    för de kommande fem dagarna från OWM-API:n'''
    lat = geolocation[0]
    lon = geolocation[1]
    api_key= "625b12521ea346774405a35cdf96d500"
    owm = OWM(api_key)
    fc = owm.three_hours_forecast_at_coords(lat, lon)
    f = fc.get_forecast()
    temp = []
    for weather in f:
        temp.append(weather.get_temperature(unit='celsius')['temp'])

    fc_temp_max = max(temp)
    fc_temp_min = min(temp)
    fc_temp_mean = mean(temp)
    rain = fc.will_have_rain()

    return fc_temp_min, fc_temp_max, fc_temp_mean, rain


def get_temp_items(forecast):
    '''Hämtar items baserad på min-/max-temperatur på destinationen'''
    fc_temp_min = forecast[0]
    fc_temp_max = forecast[1]

    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker', cursorclass=pymysql.cursors.DictCursor)
    cur = db.cursor()
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE ((%s) BETWEEN temp_min AND temp_max) OR ((%s) BETWEEN temp_min AND temp_max)", (fc_temp_min, fc_temp_max));
    item_list = cur.fetchall()
    cur.close()
    db.close()
    '''sätt antal = 1, kanske redan här? eller via en if-sats i create item_list'''

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_climate_zone(geolocation):
    '''Returnerar klimattyp för destinationens koordinater enligt Köppen-Geigers-klimatklassifikation,
    än så länge inte i bruk då API:n inte är vitlistad på pythonanywhere i nuläge, förfrågan har skickats!'''
    latitude = geolocation[0]
    longitude = geolocation[1]
    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/" + str(latitude) + "/" + str(longitude))
    data = response.json()
    result = data['return_values']
    res = result[0]
    zone = res['koppen_geiger_zone']

    return zone


def create_item_list():
    '''Lägger alla items som har hämtats från databasen i en lista med dictionaries'''
    user_input = get_user_input()
    length = get_length(user_input)
    current_or_historic = current_or_historic_weather_data(user_input)
    if current_or_historic == "current":
        which_data = "aktuell"
    else:
        which_data = "historisk"

    geolocation = get_geolocation(user_input[0])

    if current_or_historic == "current":
        forecast = get_weather_forecast(geolocation)
    else:

        zone = get_climate_zone(geolocation)
        season = get_season(user_input)
        forecast = get_historic_weather_data(zone, season)

    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    activity_items = get_activity_items(user_input[5])
    accommodation_items = get_accommodation_items(user_input[4])
    transport_items = get_transport_items(user_input[3])
    complete_list = general_items + temp_items + activity_items + accommodation_items + transport_items
    #gäller bara när koden körs på localhost
    #complete_list = []
    #for item in all_items:
    #    item_dict = {"item": item[0], "weight": item[1], "category": item[2]}
    #    complete_list.append(item_dict)

    return complete_list, user_input[0], forecast[0], forecast[1], length, which_data

@route("/")
def show_form():
    '''Returnerar startsidan'''
    return template ("index")

@route("/android/")
def generate_list_android():
    '''Genererar packlista utifrån parametrar som har skickats från Android-appen'''
    destination = request.query.param1
    transport = request.query.param2
    accommodation = request.query.param3
    activity = request.query.param4

    geolocation = get_geolocation(destination)
    forecast = get_weather_forecast(geolocation)
    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    transport_items = get_transport_items(transport)
    accommodation_items = get_accommodation_items(accommodation)
    activity_items = get_activity_items(activity)


    complete_list = general_items + temp_items + transport_items + accommodation_items + activity_items
    packlista = []
    for item in complete_list:
        packlista.append(item)

    the_list = {"lista" : packlista}
    return json.dumps(the_list)

@route("/save_list/", method="POST")
def save_list():
    '''Listan hämtas som sträng från html-formuläret. I strängen som hämtas ersätts enkla med dubbla citationstecken
    så att listan kan göras om till en lista med hjälp av json. För att bevara specialtecken läggs en picklad lista in i databasen.
    Koden som returneras är en tillfällig lösning som inte kommer att behövas när inloggning har implementerats. Kan eventuellt vara kvar
    för användare som inte vill skapa konto.'''

    item_list_to_save = getattr(request.forms, "saved_list")

    string_list = item_list_to_save
    real_list = string_list.replace("\'", "\"")
    list_reloaded = json.loads(real_list)
    code = random.randint(10000,100000)
    item_list = cloudpickle.dumps(list_reloaded)
    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute('INSERT into saved_lists VALUES (%s, %s)', (code, item_list))
    db.commit()
    cur.close()
    db.close()

    return template ('code', code = code)

@route("/get_list/")
def get_list():
    '''Hämtar en sparad lista från databasen med hjälp av koden som har genererats i webbapplikationen. OBS! ingen dict cursor'''
    list_id = request.query.param1

    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute("SELECT list FROM saved_lists WHERE id = (%s)", (list_id, ));
    item_list = cur.fetchone()
    cur.close()
    db.close()

    unpickled_item_list = cloudpickle.loads(item_list[0])
#    loaded_list = json.loads(unpickled_item_list)
    the_list = {"lista" : unpickled_item_list}

    return json.dumps(the_list)

@route("/generate_list", method="POST")
def generate_list():
    '''Genererar packlista utifrån parametrar som har skickats via webb-applikationen'''
    returns = create_item_list()
    item_list = returns[0]
    location = returns[1]
    temp_min = returns[2]
    temp_max = returns[3]
    length = returns[4]
    which_data= returns[5]
##    return json.dumps(item_list)


    return template ('test', item_list = item_list, length = length, which_data = which_data,
                     location = location, temp_min = round(temp_min), temp_max = round(temp_max))


@route("/static/<filename>")
def static_files(filename):
    return static_file (filename, root="/home/bagpacker/mysite/static")


@error(404)
def error404(error):
    return template ("error")





'''

 # -*- coding: utf-8 -*-
from bottle import default_app, route, run, template, static_file, request, redirect, error
from geopy.geocoders import Nominatim
from pyowm import OWM
from statistics import mean
import requests, json
import MySQLdb
import MySQLdb.cursors
#import cloudpickle

def get_user_input():
Hämtar användarinput från HTML-formuläret
    user_input = []
    Kolla hur man lägger till namn + value som dictionaries
    location = getattr(request.forms,"location")
    user_input.append(location)
    start = getattr(request.forms, "from")
    user_input.append(start)
    finish = getattr(request.forms, "to")
    user_input.append(finish)
OBS! måste göras om till månad för att kunna avgöra om det är sommar eller vinter
##    length = getattr(request.forms, "length")
##    user_input.append(length)
    transport = getattr(request.forms, "transport")
    user_input.append(transport)
    accommodation = getattr(request.forms, "accommodation")
    user_input.append(accommodation)
    activity = getattr(request.forms, "activity")
    user_input.append(activity)
    print(user_input)
    return user_input

def get_general_items():
    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute("SELECT item, weight, category from all_items where general = true")
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_activity_items(user_input):

    activity = user_input

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN activities ON id=item_id WHERE activity = %s', (activity, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_accommodation_items(user_input):

    accommodation = user_input

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN accommodation ON id=item_id WHERE type = %s', (accommodation, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_transport_items(user_input):

    transport = user_input

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute('SELECT item, weight, category FROM all_items LEFT OUTER JOIN transport ON id=item_id WHERE type = %s', (transport, ))
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_geolocation(user_input):
Gör om användarinput till geolocation
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input)

    return geolocation.latitude, geolocation.longitude

def get_weather_forecast(geolocation):
Hämtar max och min temperatur samt information om nederbörd
    för de kommande fem dagarna från OWM-API:n
    lat = geolocation[0]
    lon = geolocation[1]
    api_key= "625b12521ea346774405a35cdf96d500"
    owm = OWM(api_key)
    fc = owm.three_hours_forecast_at_coords(lat, lon)
    f = fc.get_forecast()
    temp = []
    for weather in f:
        temp.append(weather.get_temperature(unit='celsius')['temp'])

    fc_temp_max = max(temp)
    fc_temp_min = min(temp)
    fc_temp_mean = mean(temp)
    rain = fc.will_have_rain()

    return fc_temp_max, fc_temp_min, fc_temp_mean, rain


def get_temp_items(forecast):
Hämtar items baserad på min-/max-temperatur på destinationen
    fc_temp_max = forecast[0]
    fc_temp_min = forecast[1]

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE ((%s) BETWEEN temp_min AND temp_max) OR ((%s) BETWEEN temp_min AND temp_max)", (fc_temp_min, fc_temp_max));
    item_list = cur.fetchall()
    cur.close()
    db.close()
sätt antal = 1, kanske redan här? eller via en if-sats i create item_list

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_climate_zone(geolocation):
Returnerar klimattyp för destinationens koordinater enligt Köppen-Geigers-klimatklassifikation, än så länge inte i bruk!
    latitude = geolocation[0]
    longitude = geolocation[1]
    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/" + str(latitude) + "/" + str(longitude))
    data = response.json()
    result = data['return_values']
    res = result[0]
    zone = res['koppen_geiger_zone']

    return zone


def create_item_list():
Lägger alla items som har hämtats från databasen i en lista med dictionaries
    user_input = get_user_input()
    geolocation = get_geolocation(user_input[0])
Lägg till if sats: om traveldate ==....
    forecast = get_weather_forecast(geolocation)
    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    activity_items = get_activity_items(user_input[5])
    accommodation_items = get_accommodation_items(user_input[4])
    transport_items = get_transport_items(user_input[3])
    complete_list = general_items + temp_items + activity_items + accommodation_items + transport_items
    #complete_list = []
    #for item in all_items:
    #    item_dict = {"item": item[0], "weight": item[1], "category": item[2]}
    #    complete_list.append(item_dict)

    return complete_list, user_input[0], forecast[1], forecast[0]

@route("/")
def show_form():
Returnerar startsidan
    return template ("index")

@route("/android/")
def generate_list_android():
Genererar packlista utifrån parametrar som har skickats från Android-appen
    destination = request.query.param1
    transport = request.query.param2
    accommodation = request.query.param3
    activity = request.query.param4

    geolocation = get_geolocation(destination)
    forecast = get_weather_forecast(geolocation)
    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    transport_items = get_transport_items(transport)
    accommodation_items = get_accommodation_items(accommodation)
    activity_items = get_activity_items(activity)


    complete_list = general_items + temp_items + transport_items + accommodation_items + activity_items
    packlista = []
    for item in complete_list:
        packlista.append(item)

    the_list = {"lista" : packlista}
    return json.dumps(the_list)

@route("/get_list/")
def get_list():

    list_id = request.query.param1

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute("SELECT list FROM saved_lists WHERE id = (%s)", (list_id, ));
    item_list = cur.fetchone()
    cur.close()
    db.close()

    returned_list = item_list[0]
    #unpickled_item_list = cloudpickle.loads(item_list[0])

    return returned_list

@route("/generate_list", method="POST")
def generate_list():
Genererar packlista utifrån aktuell temperatur utifrån parametrar som har skickats via webb-applikationen
    returns = create_item_list()
    item_list = returns[0]
    location = returns[1]
    temp_min = returns[2]
    temp_max = returns[3]
##    return json.dumps(item_list)


    return template ('test', item_list = item_list,
                     location = location, temp_min = round(temp_min), temp_max = round(temp_max))


@route("/static/<filename>")
def static_files(filename):
    return static_file (filename, root="/home/bagpacker/mysite/static")


@error(404)
def error404(error):
    return template ("error")
'''

application = default_app()

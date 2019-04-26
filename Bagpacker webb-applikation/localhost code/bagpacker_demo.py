'''
    File name: bagpacker_demo_v.2.py
    Author: Christina Knepper
    Python Version: 3.7.1
'''

from bottle import default_app, route, run, template, static_file, request, redirect, error
from geopy.geocoders import Nominatim
from pyowm import OWM
from statistics import mean
import random
import requests, json
import psycopg2
import psycopg2.extras
import cloudpickle
from datetime import datetime
from datetime import date
##import MySQLdb



def get_user_input():
    '''Hämtar användarinput från HTML-formuläret, kan snyggas till genom att lägga in värden i dictionary istället för lista'''
    user_input = []
    '''Kolla hur man lägger till namn + value som dictionaries, splitta location!!! bara första ordet!!!'''
    location = getattr(request.forms,"location")
    user_input.append(location)
    start = getattr(request.forms, "from")
    user_input.append(start)
    finish = getattr(request.forms, "to")
    user_input.append(finish)

    transport = []

    if getattr(request.forms, "transport_1") == "bil":
        transport.append("bil")
    if getattr(request.forms, "transport_2") == "tåg":
        transport.append("tåg")
    if getattr(request.forms, "transport_3") == "flyg":
        transport.append("flyg")
   
    user_input.append(transport)
    accommodation = getattr(request.forms, "accommodation")
    user_input.append(accommodation)
    activity = getattr(request.forms, "activity")
    user_input.append(activity)

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

def current_or_historic_weather_data(user_input):
    '''Lägg till i pythonanywhere. Avgör om resan påbörjas inom de närmsta fem dagarna,
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
    '''Lägg till i pythonanywhere. Avgör om resan görs sommar eller vintertid, OBS! hur hanterar vi gränsfall?'''
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
    '''Lägg till i pythonanywhere. Hämtar historisk väderdata baserad på klimatzon och årstid'''
    zone = zone
    season = season
    if season == "winter":
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_winter, max_temp_winter, rain_winter from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        return forecast[0]

    else:
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_summer, max_temp_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        return forecast[0]
    
def get_general_items():
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT item, weight, category from all_items where general = true")
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_activity_items(user_input):

    activity = user_input[5]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN activities ON id=item_id WHERE activity = (%s)", (activity, ))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_accommodation_items(user_input):

    accommodation = user_input[4]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN accommodation ON id=item_id WHERE type = (%s)", (accommodation, ))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_transport_items(user_input):

    transport = user_input[3]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN transport ON id=item_id WHERE type IN ('%s')" % "','".join(transport))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list
    
def get_geolocation(user_input):
    '''Gör om användarinput till geolocation'''
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input[0])
    
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
    
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE (%s BETWEEN temp_min AND temp_max) OR (%s BETWEEN temp_min AND temp_max);", (fc_temp_min, fc_temp_max));
    item_list = cur.fetchall()
    cur.close()
    conn.close()
    '''sätt antal = 1, kanske redan här? eller via en if-sats i create item_list'''

    return item_list

def get_climate_zone(geolocation):
    '''Returnerar klimattyp för destinationens koordinater enligt Köppen-Geigers-klimatklassifikation'''
    latitude = geolocation[0]
    longitude = geolocation[1]
    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/" + str(latitude) + "/" + str(longitude))
    data = response.json()
    result = data['return_values']
    res = result[0]
    zone = res['koppen_geiger_zone']
    print(latitude)
    print(longitude)
    print(response)
    print(zone)
    return zone


def create_item_list():
    '''Lägg till i pythonanywhere. Lägger alla items som har hämtats från databasen i en lista med dictionaries'''
    user_input = get_user_input()
    length = get_length(user_input)
    current_or_historic = current_or_historic_weather_data(user_input)
    if current_or_historic == "current":
        which_data = "aktuell"
    else:
        which_data = "historisk"
    geolocation = get_geolocation(user_input)
    if current_or_historic == "current":
        forecast = get_weather_forecast(geolocation)
    else:
        zone = get_climate_zone(geolocation)
        season = get_season(user_input)
        forecast = get_historic_weather_data(zone, season)
        
    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    activity_items = get_activity_items(user_input)
    accommodation_items = get_accommodation_items(user_input)
    transport_items = get_transport_items(user_input)
    all_items = general_items + temp_items + activity_items + accommodation_items + transport_items
    complete_list = []
    for item in all_items:
        item_dict = {"item": item[0], "weight": item[1], "category": item[2]}
        complete_list.append(item_dict)

    return complete_list, user_input[0], forecast[0], forecast[1], length, which_data

@route("/")
def show_form():
    '''Returnerar startsidan, try innehåller ett tillfälligt tillägg för att kunna testa Android applikationen'''
##    try:
##        request.query["json"]
##        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker')
##        cur = db.cursor()
##        cur.execute('SELECT item from temperature')
##        result = cur.fetchall()
##        cur.close()
##        db.close()
##        item_list = []
##        for i in result:
##            for x in i:
##                item_list.append(x)
##        return json.dumps(item_list)
##    except:
##        pass
##

    return template ("index")

@route("/generate_list", method="POST")
def generate_list():
    '''Genererar packlista utifrån aktuell temperatur,
    kommer att delas upp i fler funktioner i nästa version'''
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

'''OBS! BARA TEST'''
@route("/get_list/")
def get_list_from_db():

    list_id = request.query.param1
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT list FROM saved_lists WHERE id = (%s)", (list_id, ))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    returned_list = item_list[0]

    return returned_list[0]

@route("/save_list/", method="POST")
def save_list():
    '''Listan hämtas som sträng från html-formuläret, som har fel format, måste göras om till lista innan den kan picklas, för att göra om till json måste man ha dubbla citationstecken'''
    item_list_to_save = getattr(request.forms, "saved_list")
    print(item_list_to_save)
    print(type(item_list_to_save))
    test_list = item_list_to_save
    test = test_list.replace("\'", "\"")
    print(test)
    code = random.randint(10000,100000)
    list_reloaded = json.loads(test)
    print(list_reloaded)
    print(type(list_reloaded))
    '''Detta är en lista nu!!!, skicka till databasen!'''
    item_list = cloudpickle.dumps(list_reloaded)
    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute('INSERT into saved_lists VALUES (%s, %s)', (code, item_list))
    db.commit()
    cur.close()
    db.close()

    return template ('code', code = code)
    

@route("/static/<filename>")
def static_files(filename):
    return static_file (filename, root="static")

##
##@error(404)
##def error404(error):
##    return template ("error")


run(host="localhost", port=8080, debug=True)

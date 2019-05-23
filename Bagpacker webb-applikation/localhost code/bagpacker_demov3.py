'''
    File name: bagpacker_demo_v.3.py
    Author: Christina Knepper
    Python Version: 3.7.1
'''

from bottle import default_app, route, run, template, static_file, request, redirect, error, post
from geopy.geocoders import Nominatim
#from pyowm import OWM
#from statistics import mean
import random
import requests, json
import psycopg2
import psycopg2.extras
import userinput as ui
import weather as w
import generatelist as gl

from datetime import datetime
from datetime import date




##def get_user_input():
##    '''Hämtar användarinput från HTML-formuläret, kan snyggas till genom att lägga in värden i dictionary istället för lista'''
##    user_input = []
##    '''Kolla hur man lägger till namn + value som dictionaries, splitta location!!! bara första ordet!!!'''
##    location = getattr(request.forms,"destination")
##    user_input.append(location)
##    print(location)
##    start = getattr(request.forms, "from")
##    user_input.append(start)
##    print(start)
##    finish = getattr(request.forms, "to")
##    user_input.append(finish)
##    print(finish)
##    transport = request.forms.getall("transport")
##    user_input.append(transport)
##    print(transport)
##    accommodation = request.forms.getall("accommodation")
##    user_input.append(accommodation)
##    print(accommodation)
##    activity = request.forms.getall("activities")
##    user_input.append(activity)
##    print(activity)
##    
##
##    return user_input
##
##def get_length(user_input):
##    '''Lägg till i pythonanywhere. Räknar ut resans längd i dagar utifrån användarens datum inmatning, om större än 14 utgår vi ifrån att man tvättar!!!'''
##    start = user_input[1]
##    finish = user_input[2]
##
##    dt_start = datetime.strptime(start, "%Y/%m/%d")
##    dt_finish = datetime.strptime(finish, "%Y/%m/%d")
##    date_start = dt_start.date()
##    date_finish = dt_finish.date()
##    length = date_finish - date_start
##
##    return length.days
##
##def current_or_historic_weather_data(user_input):
##    '''Lägg till i pythonanywhere. Avgör om resan påbörjas inom de närmsta fem dagarna,
##    måste även vara beroende av resans längd!!!, ev. kombination av KG och aktuell prognos i så fall?'''
##    start = user_input[1]
##    dt_start = datetime.strptime(start, "%Y/%m/%d")
##    date_start = dt_start.date()
##    today = date.today()
##    
##    print(date_start)
##    print(today)
##    difference = date_start - today
##    print(difference.days)
##    r = range(0,6)
##    if difference.days in r:
##        return "current"
##    else:
##        return "historic"
##
##def get_season(user_input, geolocation):
##    '''OBS! nya ändringar, hur gör vi när någon är iväg över månadsgränsen, räkna ut hur många dagar i vilken månad?'''
##    '''Lägg till i pythonanywhere. Avgör om resan görs sommar eller vintertid, OBS! hur hanterar vi gränsfall?'''
##    start = user_input[1]
##    finish = user_input[2]
##
##    dt1 = datetime.strptime(start, "%Y/%m/%d")
##    dt2 = datetime.strptime(finish, "%Y/%m/%d")
##    month1 = dt1.month
##    month2 = dt2.month
##    print(month1)
##    print(month2)
##    month = month1
##    
##    if month in range(3,6):
##        if geolocation[0] <= 0:
##            return "autumn"
##        else:
##            return "spring"
##    elif month in range(6,9):
##        if geolocation[0] <= 0:
##            return "winter"
##        else:
##            return "summer"
##    elif month in range(9,12):
##        if geolocation[0] <= 0:
##            return "spring"
##        else:
##            return "autumn"
##    else:
##        if geolocation[0] <= 0:
##            return "summer"
##        else:
##            return "winter"
##        
##    
##def get_historic_weather_data(zone, season):
##    '''Lägg till i pythonanywhere. Hämtar historisk väderdata baserad på klimatzon och årstid'''
##    zone = zone
##    season = season
##    if season == "winter":
##        conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##        cur.execute("SELECT min_temp_winter, max_temp_winter, rain_winter from climate_classification where zone = (%s)", (zone, ))
##        forecast = cur.fetchall()
##        cur.close()
##        conn.close()
##        return forecast[0]
##
##    elif season == "summer":
##        conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##        cur.execute("SELECT min_temp_summer, max_temp_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
##        forecast = cur.fetchall()
##        cur.close()
##        conn.close()
##        return forecast[0]
##
##    elif season == "spring":
##        conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##        cur.execute("SELECT min_temp_spring, max_temp_spring, rain_spring from climate_classification where zone = (%s)", (zone, ))
##        forecast = cur.fetchall()
##        cur.close()
##        conn.close()
##        return forecast[0]
##    
##    elif season == "autumn":
##        conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##        cur.execute("SELECT min_temp_autumn, max_temp_autumn, rain_autumn from climate_classification where zone = (%s)", (zone, ))
##        forecast = cur.fetchall()
##        cur.close()
##        conn.close()
##        return forecast[0]
##    
##def get_general_items():
##    conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##    cur.execute("SELECT id, item, weight, category, quantity from all_items where general = true")
##    item_list = cur.fetchall()
##    cur.close()
##    conn.close()
##
##    return item_list
##
##def get_activity_items(user_input):
##    '''OBS! Ändring, måste läggas till i pythonanywhere'''
##    activity = user_input[5]
##
##    conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN activities ON id=item_id WHERE activity IN ('%s')" % "','".join(activity))
##    item_list = cur.fetchall()
##    cur.close()
##    conn.close()
##
##    return item_list
##
##def get_accommodation_items(user_input):
##    '''OBS! Ändring, måste läggas till i pythonanywhere'''
##    accommodation = user_input[4]
##
##    conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN accommodation ON id=item_id WHERE type IN ('%s')" % "','".join(accommodation))
##    item_list = cur.fetchall()
##    cur.close()
##    conn.close()
##
##    return item_list
##
##def get_transport_items(user_input):
##    '''OBS! Ändring, måste läggas till i pythonanywhere'''
##    transport = user_input[3]
##
##    conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN transport ON id=item_id WHERE type IN ('%s')" % "','".join(transport))
##    item_list = cur.fetchall()
##    cur.close()
##    conn.close()
##
##    return item_list
##    
##def get_geolocation(user_input):
##    '''Gör om användarinput till geolocation'''
##    geolocator = Nominatim(user_agent = "Bagpacker")
##    geolocation = geolocator.geocode(user_input[0])
##    
##    return geolocation.latitude, geolocation.longitude
##
##def get_weather_forecast(geolocation):
##    '''Hämtar max och min temperatur samt information om nederbörd
##    för de kommande fem dagarna från OWM-API:n'''
##    lat = geolocation[0]
##    lon = geolocation[1]
##    api_key= "625b12521ea346774405a35cdf96d500"
##    owm = OWM(api_key)
##    fc = owm.three_hours_forecast_at_coords(lat, lon)
##    f = fc.get_forecast()
##    temp = []
##    for weather in f:
##        temp.append(weather.get_temperature(unit='celsius')['temp'])
##
##    fc_temp_max = max(temp)
##    fc_temp_min = min(temp)
##    fc_temp_mean = mean(temp)
##    rain = fc.will_have_rain()
##    
##    return fc_temp_min, fc_temp_max, fc_temp_mean, rain
##
##    
##def get_temp_items(forecast):
##    '''OBS! lagt till other_dependencies på test för att utesluta att alla items som är temperaturberoende kommer med'''
##    '''Hämtar items baserad på min-/max-temperatur på destinationen'''
##    fc_temp_min = forecast[0]
##    fc_temp_max = forecast[1]
##    
##    conn = psycopg2.connect(host="pgserver.mah.se",
##                            database="bagpacker", user="ai8134", password="h9rbyai5")
##    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE ((%s BETWEEN temp_min AND temp_max) OR (%s BETWEEN temp_min AND temp_max)) AND other_dependencies=FALSE;", (fc_temp_min, fc_temp_max));
##    item_list = cur.fetchall()
##    cur.close()
##    conn.close()
##    '''sätt antal = 1, kanske redan här? eller via en if-sats i create item_list'''
##
##    return item_list
##
##def get_climate_zone(geolocation):
##    '''Returnerar klimattyp för destinationens koordinater enligt Köppen-Geigers-klimatklassifikation'''
##    latitude = geolocation[0]
##    longitude = geolocation[1]
##    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/" + str(latitude) + "/" + str(longitude))
##    data = response.json()
##    result = data['return_values']
##    res = result[0]
##    zone = res['koppen_geiger_zone']
##    print(latitude)
##    print(longitude)
##    print(response)
##    print(zone)
##    return zone
##
##def get_weight(complete_list):
##    weight_list = []
##    for item in complete_list:
##        item_weight = item["weight"]*item["quantity"]
##        weight_list.append(item_weight)
##    total_weight = sum(weight_list)
##    print(total_weight)
##    return round(total_weight, 2)
##
##def create_item_list(user_input):
##    '''Lägg till i pythonanywhere. Lägger alla items som har hämtats från databasen i en lista med dictionaries'''
##    length = get_length(user_input)
##    current_or_historic = current_or_historic_weather_data(user_input)
##    if current_or_historic == "current":
##        which_data = "aktuell"
##    else:
##        which_data = "historisk"
##    geolocation = get_geolocation(user_input)
##    if current_or_historic == "current":
##        forecast = get_weather_forecast(geolocation)
##        print(forecast)
##        rain = forecast[3]
##    else:
##        zone = get_climate_zone(geolocation)
##        season = get_season(user_input, geolocation)
##        forecast = get_historic_weather_data(zone, season)
##        rain = forecast[2]
##        
##    general_items = get_general_items()
##    temp_items = get_temp_items(forecast)
##    activity_items = get_activity_items(user_input)
##    accommodation_items = get_accommodation_items(user_input)
##    transport_items = get_transport_items(user_input)
##    all_items = general_items + temp_items + activity_items + accommodation_items + transport_items
##    all_items_without_duplicates = []
##    
##    for sublist in all_items:
##        if sublist not in all_items_without_duplicates:
##            all_items_without_duplicates.append(sublist)
##    complete_list = []
##
##    if length >= 14:
##        factor = 14
##    else:
##        factor = length
##    
##    for item in all_items_without_duplicates:
##        quantity = item[4]*factor + 1
##        item_dict = {"id": item[0], "item": item[1], "weight": item[2], "category": item[3], "quantity": round(quantity)}
##        complete_list.append(item_dict)
##
##    total_weight = get_weight(complete_list)
##
##        
####        item_dict = {"item": item[0], "weight": item[1], "category": item[2]}
####        complete_list.append(item_dict)
##    
##    return complete_list, user_input[0], forecast[0], forecast[1], length, which_data, total_weight, rain
        
@route("/")
def show_startpage():
    '''Returnerar startsidan'''
    return template ("index")

@route("/newlist")
def show_form():
    '''Returnerar formulär'''
    
    return template ("javascriptform")

@route("/validate_destination", method="POST")
def validate_destination():
    '''Validerar destinationen'''
    destination = getattr(request.forms, "destination")
    print(destination)
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(destination)

    try:
        lat = geolocation.latitude
        message = {"valid" : True}
        return json.dumps(message)

    except:
        message = {"valid" : False, "message" : "Destinationen du har angett är inte giltig."}
        return json.dumps(message)
    

@route("/generate_list", method="POST")
def generate_list():
    '''Genererar packlista utifrån användarens input'''
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


@route("/generate_list2", method="POST")
def generate_list():
    '''Genererar packlista utifrån användarens input'''
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
    '''Genererar packlista utifrån parametrar som har skickats från Android-appen'''

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

    the_list = {"lista" : complete_list, "destination" : destination, "temp_min" : temp_min, "temp_max" : temp_max, "length" : length, "weather_data" : weather_data}
    return json.dumps(the_list)
 

@route("/get_saved_list/")
def get_saved_list():
#aktuell!!!
    list_id = request.query.param1
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, saved_lists.quantity FROM saved_lists JOIN all_items on item_id=id WHERE code = (%s)", (list_id, ))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    complete_list = []
    for item in item_list:
        item_dict = {"id": item[0], "item": item[1], "weight": item[2], "category": item[3], "quantity": item[4]}
        complete_list.append(item_dict)

#total_weight = get_weight(complete_list)

    return json.dumps(complete_list)

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
#aktuell
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
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor()
    for item in complete_list:
        cur.execute('INSERT into saved_lists VALUES (%s, %s, %s, %s)', (code, item['id'], item['quantity'], item['checked']))
    conn.commit()
    cur.close()
    conn.close()

    destination = getattr(request.forms, "destination")
    temp_min = int(getattr(request.forms, "temp_min"))
    temp_max = int(getattr(request.forms, "temp_max"))
    temp_mean = int(getattr(request.forms, "temp_mean"))
    which_data = getattr(request.forms, "which_data")
    rain_risk = getattr(request.forms, "rain_risk")
    print(rain_risk)
    if rain_risk == "hög":
        rain = True
    else:
        rain = False

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor()
    cur.execute('INSERT into weather_data VALUES (%s, %s, %s, %s, %s, %s, %s)', (code, destination, temp_min, temp_max, temp_mean, which_data, rain))
    conn.commit()
    cur.close()
    conn.close()
    return json.dumps(code)

@route("/edit_list/", method="POST")
def edit_list():
    item_list_to_edit = getattr(request.forms, "saved_list")
    item_list = json.loads(item_list_to_edit.replace("\'", "\""))
    print(item_list)
    
    return template('edit_list', item_list = item_list)

## Tillfällig lösning: lista sparas som sträng och kan hämtas med hjälp av en kod
##@route("/save_list/", method="POST")
##def save_list():
##    '''Listan hämtas som sträng från html-formuläret, som har fel format, måste göras om till lista innan den kan picklas, för att göra om till json måste man ha dubbla citationstecken'''
##    item_list_to_save = getattr(request.forms, "saved_list")
##    print(item_list_to_save)
##    print(type(item_list_to_save))
##    test_list = item_list_to_save
##    test = test_list.replace("\'", "\"")
##    print(test)
##    code = random.randint(10000,100000)
##    list_reloaded = json.loads(test)
##    print(list_reloaded)
##    print(type(list_reloaded))
##    '''Detta är en lista nu!!!, skicka till databasen!'''
##    item_list = cloudpickle.dumps(list_reloaded)
##    db = pymysql.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
##    cur = db.cursor()
##    cur.execute('INSERT into saved_lists VALUES (%s, %s)', (code, item_list))
##    db.commit()
##    cur.close()
##    db.close()
##
##    return template ('code', code = code)
##    

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

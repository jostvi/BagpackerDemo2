from bottle import default_app, route, run, template, static_file, request, redirect, error
from geopy.geocoders import Nominatim
import requests, json
import MySQLdb

def get_geolocation(user_input):
    '''Gör om användarinput till geolocation'''
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input)
    return geolocation.latitude, geolocation.longitude

def get_weather_forecast(geolocation):
    '''Hämtar väderdata från OWM-API'''
    latitude = geolocation[0]
    longitude = geolocation[1]
    api_key= "m_key"
    base_url = "https://api.openweathermap.org/data/2.5/weather?"
    complete_url = base_url + "lat=" + str(latitude) + "&lon=" + str(longitude) + "&units=metric" + "&lang=se" "&appid=" + api_key
    response = requests.get(complete_url)
    weather_data = response.json()
    main_data = weather_data['main']
    temperature = round(main_data['temp'])
    return temperature


@route("/")
def show_form():
    '''Returnerar startsidan'''
    try:
    '''Tillfälligt tillägg för att kunna testa Android-applikationen'''
        request.query["json"]
        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature')
        result = cur.fetchall()
        cur.close()
        db.close()
        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)
        return json.dumps(item_list)
    except:
        pass


    return template ("index")

@route("/generate_list", method="POST")
def generate_list():
    '''Genererar packlista utifrån aktuell temperatur, kommer att delas upp i fler funktioner i nästa version'''

    location = getattr(request.forms,"location")
    geolocation = get_geolocation(location)
    temperature = get_weather_forecast(geolocation)

    if temperature <= 5:

        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature WHERE temp_limit <=5')
        result = cur.fetchall()
        cur.close()
        db.close()

        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)

    elif temperature > 5 and temperature <= 10:

        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature WHERE temp_limit >5 and temp_limit<=10')
        result = cur.fetchall()
        cur.close()
        db.close()

        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)

    elif temperature > 10 and temperature <= 15:

        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature WHERE temp_limit >10 and temp_limit<=15')
        result = cur.fetchall()
        cur.close()
        db.close()

        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)

    elif temperature > 15 and temperature < 20:

        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature WHERE temp_limit >15 and temp_limit<20')
        result = cur.fetchall()
        cur.close()
        db.close()

        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)

    elif temperature >=20:

        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='???s', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute('SELECT item from temperature WHERE temp_limit >=20')
        result = cur.fetchall()
        cur.close()
        db.close()

        item_list = []
        for i in result:
            for x in i:
                item_list.append(x)



    return template ('test', temperature=temperature, location=location, item_list=item_list)


@route("/static/<filename>")
def static_files(filename):
    return static_file (filename, root="/home/bagpacker/mysite/static")


@error(404)
def error404(error):
    return template ("error")

application = default_app()


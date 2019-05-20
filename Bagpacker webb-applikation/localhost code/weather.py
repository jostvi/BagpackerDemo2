from geopy.geocoders import Nominatim
from pyowm import OWM
import requests, json
from statistics import mean
import psycopg2
import psycopg2.extras
from datetime import datetime
from datetime import date

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

def get_season(user_input, geolocation):
    '''OBS! nya ändringar, hur gör vi när någon är iväg över månadsgränsen, räkna ut hur många dagar i vilken månad?'''
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
    
    if month in range(3,6):
        if geolocation[0] <= 0:
            return "autumn"
        else:
            return "spring"
    elif month in range(6,9):
        if geolocation[0] <= 0:
            return "winter"
        else:
            return "summer"
    elif month in range(9,12):
        if geolocation[0] <= 0:
            return "spring"
        else:
            return "autumn"
    else:
        if geolocation[0] <= 0:
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

    elif season == "summer":
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_summer, max_temp_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        return forecast[0]

    elif season == "spring":
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_spring, max_temp_spring, rain_spring from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        return forecast[0]
    
    elif season == "autumn":
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_autumn, max_temp_autumn, rain_autumn from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        return forecast[0]
    

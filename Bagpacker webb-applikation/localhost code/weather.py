from geopy.geocoders import Nominatim
from pyowm import OWM
import requests, json
from statistics import mean
import psycopg2
import psycopg2.extras
from datetime import datetime
from datetime import date

def get_geolocation(user_input):
    '''Transforms user input (destination) to geolocation'''
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input[0])
    
    return geolocation.latitude, geolocation.longitude

def get_weather_forecast(geolocation):
    '''Fetches min, max, mean temperature and precipitation information
    for the following five days from OWM-API'''

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
    '''Returns a destination's climate zone according to Köppen & Geigers climate classification'''
    latitude = geolocation[0]
    longitude = geolocation[1]
    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/"
                            + str(latitude) + "/" + str(longitude))
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
    ''' Returns current or historic depending on startdate for the trip.'''
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
    '''Returns season for the trip based on destination, latitude and date'''
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
    '''Fetches historic weather data from the database based on climate zone and season'''
    zone = zone
    season = season
    if season == "winter":
        conn = db.db_connection()
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_winter, max_temp_winter, mean_winter, rain_winter from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        print(forecast[0])
        return forecast[0]

    elif season == "summer":
        conn = db.db_connection()
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_summer, max_temp_summer, mean_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        print(forecast[0])
        return forecast[0]

    elif season == "spring":
        conn = db.db_connection()
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_spring, max_temp_spring, mean_spring, rain_spring from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        print(forecast[0])
        return forecast[0]
    
    elif season == "autumn":
        conn = db.db_connection()
        cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
        cur.execute("SELECT min_temp_autumn, max_temp_autumn, mean_autumn, rain_autumn from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        conn.close()
        print(forecast[0])
        return forecast[0]
    

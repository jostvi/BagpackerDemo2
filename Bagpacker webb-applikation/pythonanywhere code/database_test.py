import MySQLdb
import MySQLdb.cursors
import json, requests
from geopy.geocoders import Nominatim
import cloudpickle
import random
#import pymysql
#import pymysql.cursors


def get_temp_items():
    fc_temp_min = 7
    fc_temp_max = 20

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
    cur = db.cursor()
    cur.execute("SELECT item, weight, category FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE ((%s) BETWEEN temp_min AND temp_max) OR ((%s) BETWEEN temp_min AND temp_max)", (fc_temp_min, fc_temp_max));
    item_list = cur.fetchall()
    cur.close()
    db.close()

    result = []
    for item in item_list:
        result.append(item)
    return result

def get_transport_items():

    transport = "bil"

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

def create_item_list():

    temp_items = get_temp_items()

    transport_items = get_transport_items()
    all_items = transport_items + temp_items


    return all_items

#def save_list(item_list_to_save):
#    item_list = cloudpickle.dumps(item_list_to_save)
#    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker', cursorclass=MySQLdb.cursors.DictCursor)
#    cur = db.cursor()
#    cur.execute('INSERT into saved_lists (id, list) VALUES (123, %s)', (item_list, ))
#    db.commit()
#    cur.close()
#    db.close()

#def get_list(id):

#    list_id = id

#    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker')
#    cur = db.cursor()
#    cur.execute("SELECT list FROM saved_lists WHERE id = (%s)", (list_id, ));
#    item_list = cur.fetchone()
#    cur.close()
#    db.close()

#    unpickled_item_list = cloudpickle.loads(item_list[0])
#    print(unpickled_item_list)

#    the_list = {"lista" : unpickled_item_list}
#    return the_list

def get_list(id):
    list_id = id

    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', passwd='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute("SELECT list FROM saved_lists WHERE id = (%s)", (list_id, ));
    item_list = cur.fetchone()
    cur.close()
    db.close()
    print(item_list)
    unpickled_item_list = cloudpickle.loads(item_list[0])
    print(unpickled_item_list)
    print(type(unpickled_item_list))
#    dumped_list = json.dumps(unpickled_item_list)
#    loaded_list = json.loads(dumped_list)
#    the_list = {"lista" : loaded_list}
#    print(the_list)

get_list(123)

#item_list = create_item_list()
#save_list(item_list)
#saved_list = get_list(123)
#print(saved_list)

#print(random.randint(1000,10000))

def save_list(item_list_to_save):
#    item_list_to_save = getattr(request.forms, "saved_list")
    list_id = random.randint(10000,100000)
    print(list_id)
    item_list = cloudpickle.dumps(item_list_to_save)
    db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
    cur = db.cursor()
    cur.execute('INSERT into saved_lists VALUES (%s, %s)', (list_id, item_list))
    db.commit()
    cur.close()
    db.close()

    return list_id

#item_list = create_item_list()
#code = save_list(item_list)
#print(code)
#saved_list = get_list(code)

#print(saved_list)

#test_list = "[{'item': 'skor', 'weight': 0.8, 'category': 'kl\u00e4der'}, {'item': 'pyjamas', 'weight': 0.5, 'category': 'kl\u00e4der'}, {'item': 'underkl\u00e4der', 'weight': 0.2, 'category': 'kl\u00e4der'}, {'item': 'deo', 'weight': 0.1, 'category': 'necess\u00e4r'}, {'item': 'tandborste', 'weight': 0.02, 'category': 'necess\u00e4r'}, {'item': 'tandkr\u00e4m', 'weight': 0.05, 'category': 'necess\u00e4r'}, {'item': 'k\u00f6rkort', 'weight': 0.01, 'category': 'resehandlingar'}, {'item': 'pass', 'weight': 0.02, 'category': 'resehandlingar'}, {'item': 'kreditkort', 'weight': 0.01, 'category': 'resehandlingar'}, {'item': 'laddare', 'weight': 0.1, 'category': 'elektronik'}, {'item': 'mobiltelefon', 'weight': 0.12, 'category': 'elektronik'}, {'item': 'h\u00f6rlurar', 'weight': 0.05, 'category': 'elektronik'}, {'item': 'powerbank', 'weight': 0.05, 'category': 'elektronik'}, {'item': 'nackkudde', 'weight': 0.2, 'category': '\u00f6vrigt'}, {'item': 'sandaler', 'weight': 0.5, 'category': 'kl\u00e4der'}, {'item': 'sollinne', 'weight': 0.1, 'category': 'kl\u00e4der'}, {'item': 'solhatt / keps', 'weight': 0.1, 'category': 'kl\u00e4der'}, {'item': 'kortbyxor', 'weight': 0.1, 'category': 'kl\u00e4der'}, {'item': 'badhandduk', 'weight': 0.3, 'category': '\u00f6vrigt'}, {'item': 'strandv\u00e4ska', 'weight': 0.3, 'category': '\u00f6vrigt'}, {'item': 'snorkel & cyklop', 'weight': 0.3, 'category': 'specialutrustning'}, {'item': 'schampo', 'weight': 0.2, 'category': 'necess\u00e4r'}, {'item': 'tv\u00e5l', 'weight': 0.2, 'category': 'necess\u00e4r'}, {'item': 'flygbiljetter', 'weight': 0.01, 'category': 'resehandlingar'}]"
#test = json.loads(test_list)
#test = json.dumps(test_list)
#test2 = json.loads(test)
#print(type(test_list))
#print(test)
#print(type(test))
#print(test2)
#print(type(test2))
#code = save_list(test2)
#saved_list = get_list(code)


def get_geolocation(user_input):
    '''Gör om användarinput till geolocation'''
    geolocator = Nominatim(user_agent = "Bagpacker")
    geolocation = geolocator.geocode(user_input)
    return geolocation.latitude, geolocation.longitude

def get_climate_zone(geolocation):
    '''Returnerar klimattyp för destinationens koordinater enligt Köppen-Geigers-klimatklassifikation,
    än så länge inte i bruk då API:n inte är vitlistad på pythonanywhere i nuläge, förfrågan har skickats!'''
    latitude = geolocation[0]
    longitude = geolocation[1]
    response = requests.get("http://climateapi.scottpinkelman.com/api/v1/location/" + str(latitude) + "/" + str(longitude))
    print(latitude)
    print(longitude)
    print(response)
    data = response.json()
    result = data['return_values']

    res = result[0]
    zone = res['koppen_geiger_zone']
#    return response
    return zone

#user_input = "San Francisco"
#geolocation = get_geolocation(user_input)
#zone = get_climate_zone(geolocation)
#print(zone)

def get_historic_weather_data(zone, season):
    '''Hämtar historisk väderdata baserad på klimatzon och årstid'''
    zone = zone
    season = season
    if season == "winter":
        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute("SELECT min_temp_winter, max_temp_winter, rain_winter from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        db.close()
        return forecast[0]

    else:
        db = MySQLdb.connect(host='bagpacker.mysql.pythonanywhere-services.com', user='bagpacker', password='bpdb2019', db='bagpacker$bagpacker')
        cur = db.cursor()
        cur.execute("SELECT min_temp_summer, max_temp_summer, rain_summer from climate_classification where zone = (%s)", (zone, ))
        forecast = cur.fetchall()
        cur.close()
        db.close()
        return forecast[0]

#zone = "Csb"
#season = "summer"
#data = get_historic_weather_data(zone, season)
#print(data)
#print(data[0])

import psycopg2
import psycopg2.extras
import weather as w
import userinput as ui

def get_general_items():
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, quantity from all_items where general = true")
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_activity_items(user_input):
    '''OBS! Ändring, måste läggas till i pythonanywhere'''
    activity = user_input[5]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN activities ON id=item_id WHERE activity IN ('%s')" % "','".join(activity))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_accommodation_items(user_input):
    '''OBS! Ändring, måste läggas till i pythonanywhere'''
    accommodation = user_input[4]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN accommodation ON id=item_id WHERE type IN ('%s')" % "','".join(accommodation))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list

def get_transport_items(user_input):
    '''OBS! Ändring, måste läggas till i pythonanywhere'''
    transport = user_input[3]

    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN transport ON id=item_id WHERE type IN ('%s')" % "','".join(transport))
    item_list = cur.fetchall()
    cur.close()
    conn.close()

    return item_list


def get_temp_items(forecast):
    '''OBS! lagt till other_dependencies på test för att utesluta att alla items som är temperaturberoende kommer med'''
    '''Hämtar items baserad på min-/max-temperatur på destinationen'''
    fc_temp_min = forecast[0]
    fc_temp_max = forecast[1]
    temp_mean = forecast[2]
    
    conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
    cur = conn.cursor(cursor_factory = psycopg2.extras.DictCursor)
##cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE ((%s BETWEEN temp_min AND temp_max) OR (%s BETWEEN temp_min AND temp_max)) AND other_dependencies=FALSE;", (fc_temp_min, fc_temp_max));
    cur.execute("SELECT id, item, weight, category, quantity FROM all_items LEFT OUTER JOIN temperature ON id=item_id WHERE (%s BETWEEN temp_min AND temp_max) AND other_dependencies=FALSE;", (temp_mean, ));
    item_list = cur.fetchall()
    cur.close()
    conn.close()
    '''sätt antal = 1, kanske redan här? eller via en if-sats i create item_list'''

    return item_list

def get_weight(complete_list):
    weight_list = []
    for item in complete_list:
        item_weight = item["weight"]*item["quantity"]
        weight_list.append(item_weight)
    total_weight = sum(weight_list)
    print(total_weight)
    return round(total_weight, 2)

def create_item_list(user_input):
    '''Lägg till i pythonanywhere. Lägger alla items som har hämtats från databasen i en lista med dictionaries'''
    length = ui.get_length(user_input)
    current_or_historic = w.current_or_historic_weather_data(user_input)
    if current_or_historic == "current":
        which_data = "aktuell"
    else:
        which_data = "historisk"
    geolocation = w.get_geolocation(user_input)
    if current_or_historic == "current":
        forecast = w.get_weather_forecast(geolocation)
        print(forecast)
        rain = forecast[3]
    else:
        zone = w.get_climate_zone(geolocation)
        season = w.get_season(user_input, geolocation)
        forecast = w.get_historic_weather_data(zone, season)
        rain = forecast[3]
        
    general_items = get_general_items()
    temp_items = get_temp_items(forecast)
    activity_items = get_activity_items(user_input)
    accommodation_items = get_accommodation_items(user_input)
    transport_items = get_transport_items(user_input)
    all_items = general_items + temp_items + activity_items + accommodation_items + transport_items
    all_items_without_duplicates = []
    
    for sublist in all_items:
        if sublist not in all_items_without_duplicates:
            all_items_without_duplicates.append(sublist)
    complete_list = []

    if length >= 14:
        factor = 14
    else:
        factor = length
    
    for item in all_items_without_duplicates:
        quantity = item[4]*factor + 1
        item_dict = {"id": item[0], "item": item[1], "weight": item[2], "category": item[3], "quantity": round(quantity)}
        complete_list.append(item_dict)

    total_weight = get_weight(complete_list)
    
    return complete_list, user_input[0], forecast[0], forecast[1], length, which_data, total_weight, rain, forecast[2]


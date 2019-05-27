import psycopg2
import psycopg2.extras


def db_connection():
    '''Establishes database connection'''
    try:
        conn = psycopg2.connect(host="pgserver.mah.se",
                            database="bagpacker", user="ai8134", password="h9rbyai5")
        print("connected")
        return conn

    except:
        print ("I am unable to connect to the database")

    

    

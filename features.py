''' 
    Source : https://www.worldweatheronline.com/developer/premium-api-explorer.aspx
    API Key :  5ac9d480716d4b7192b43943192106
'''
import json 

with open('weather_historic.json') as json_file: 
    data = json.load(json_file)
    
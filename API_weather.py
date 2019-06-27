import requests
import time
import sched

url = 'http://api.openweathermap.org/data/2.5/weather?id=1261481&APPID=a1a98e4e5f70f02ddb5ea6df28f36a2f&units=metric'
page = requests.get(url)
print(page.content)


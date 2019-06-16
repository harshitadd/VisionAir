import requests as rst 
from bs4 import BeautifulSoup as bs
import csv 
import time 
import sched 

while(1):
    url = 'https://weather.com/en-IN/weather/today/'
    page = rst.get(url)
    soup=bs(page.text,'lxml')

    k=0
    prime_data=[]

    csv_file=open('WeatherData.csv','w')
    csv_writer=csv.writer(csv_file)
    #scraping the time stamp

    for time in soup.find_all('p', class_='today_nowcard-timestamp'):
        for t in time.find_all('span'):
            k+=1
            if(k%2==0):
                temp=t.text
                temp1=temp.split(' ')
                temp=temp1[0]
                
    csv_writer.writerow(['Time :',temp])

    for temp in soup.find_all('div',class_='today_nowcard-temp'):
        for t in temp.find_all('span'):
            csv_writer.writerow(['Temp :',t.text])
            

    #scraping the real time parameters 

    for prime in soup.find_all('div',class_='today_nowcard-sidecar component panel'):
        for td in prime.find_all('span',class_=''):
            prime_data.append(td.text)
        prime_data.pop(2)

    #csv_writer.writerow(map(lambda x:[x],prime_data)) 
    csv_writer.writerow(['Wind',prime_data[0]])
    csv_writer.writerow(['Humidity',prime_data[1]])
    csv_writer.writerow(['Dew Point',prime_data[2]])
    csv_writer.writerow(['Pressure',prime_data[3]])
    csv_writer.writerow(['Visibility',prime_data[4]])
    time.sleep(60)

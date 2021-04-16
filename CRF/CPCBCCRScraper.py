from selenium import webdriver 
from bs4 import BeautifulSoup
import pandas as pd
import csv  

driver = webdriver.Chrome("/home/harshita/chromedriver")
driver.get('https://app.cpcbccr.com/AQI_India/')
soup=BeautifulSoup(res,features="lxml")
# # main=soup.find('table',class_='table table-hover table-condensed')
# # print(main)
# #for head in soup.find_all('div',class_='col-md-3 form-group'):
for station in soup.find_all('select', id='stations'):
     options = station.find_all('option')
     print(options)




#CSV Writing part 

# csv_file=open('cpcbscrape.csv','w')
# csv_writer=csv.writer(csv_file)
# csv_writer.writerow(['Station Name','Date','Time','PM Value'])
# csv_writer.writerow(stationName,date,time,pm)





# for main in soup.find('tbody', class_='metrics-container'):
#     fields.append(tr.find('td',class_='element-name'))
#     print(fields)


#<td class="title">Anand Vihar, Delhi - DPCC</td>

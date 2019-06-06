import openpyxl as ox
import numpy as np
import csv 
import pandas as pd

wbr = ox.load_workbook('NSIT.xlsx')
sheetr = wbr['Sheet1']
csv_file=open('NSIT_PM.csv','w')
csv_writer=csv.writer(csv_file)
csv_writer.writerow(['0:00 - 01:00','01:00 - 02:00','02:00 - 03:00','03:00 - 04:00','04:00 - 05:00','05:00 - 06:00','06:00 - 07:00','07:00 - 08:00','08:00 - 09:00','09:00 - 10:00','10:00 - 11:00','11:00 - 12:00','12:00 - 13:00','13:00 - 14:00','14:00 - 15:00','15:00 - 16:00','16:00 - 17:00','17:00 - 18:00','18:00 - 19:00','19:00 - 20:00','20:00 - 21:00','21:00 - 22:00','22:00 - 23:00','23:00 - 00:00'])
temp=[]
k=1
j=18
i=1
while(j<3677):
    while(k%25!=0):
        r=sheetr['C'+str(j)]
        temp.append(r.value)
        j+=1
        k+=1
    csv_writer.writerow(temp)
    k=1
    temp=[]
 

#csv_writer.writerows(map(lambda x:x,temp))

# print(temp)  
# csv_writer.writerows(map(lambda x:[x],temp))

# np.resize(temp, (-1, 24))
# # print(np.shape(temp))

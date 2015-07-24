'''
Copyright 2015 Serendio Inc.
Author - Satish Palaniappan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
'''
__author__ = "Satish Palaniappan"

import pickle
from bs4 import BeautifulSoup
import urllib.request
import urllib
from urllib.request import Request, urlopen



def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)
def chunks(l, n):
    return [l[i:i + n] for i in range(0, len(l), n)]

soup = BeautifulSoup(open("acronyms.html"))

box = soup.find_all("div","list_box3")[0]

abrr = dict()

for item in box.ul.find_all("li"):
	print(item.contents)
	if len(item.contents) < 2:
		continue
	data  = item.contents[1].strip()
	data = data.replace("-or-","")
	data = data.replace(",","")
	data = data.replace("it means","")
	data = data.lower().strip()
	abrr[item.span.a.get_text().lower()] = data


#### ext

ext = open("extAbbr.txt")

lines=ext.readlines()
lines = [l for l in lines if l[0].strip()!="("]
lines = [l.replace("\n","").lower().strip() for l in lines]
lines = [l for l in lines if l]
#print(lines)
lines = [chunks(lines,2)]

#print(lines)

for line in lines[0]:
	try:
		if abrr[line[0]]:
			#print(line[0])
			continue
	except:
		line[1] = line[1].replace("\"","")
		# line[1] = line[1].replace("'","")
		line[1] = line[1].replace("Meaning","")
		line[1] = line[1].replace("meaning","")
		line[1] = line[1].replace("means","")
		line[1] = line[1].replace("Means","")
		abrr[line[0]] = line[1]

# abrr = dict()

# for ID in range(1,26):
# 	u = "http://www.internetslang.com/list.asp?i=all&ezpage="+str(ID)
# 	req = Request(u, headers={'User-Agent': 'Mozilla/5.0'})
# 	print (u)
# 	a=0
# 	while(a<3):
# 		# try:
# 		    # url = urllib.request.urlretrieve(u)
# 		    # if url[0] == "":
# 		    #     continue
# 		    soup = BeautifulSoup(urlopen(req))
# 		    box = soup.findAll(attrs={"ezoic":"combined"})
# 		    print(box)
# 		    p = input("Continue!?")
# 		    for item,deff in zip(box.tr.find_all(attrs={'style':"background-color:transparent;font-size:14px;vertical-align:top;"}),box.tr.find_all(attrs={'style':"background-color:transparent;font-size:14px;"})):
# 		    	abrr[item.get_text().strip().lower()] = deff.get_text().strip()
# 		#     break
# 		# except:
# 		# 	print ('retrying')
# 		# 	a=a+1
# 		# 	if a==3:
# 		# 		print("ss")

save_obj(abrr,"acronymsDict")
# Test
# for a in abrr.keys():
# 	print(a + " : " + abrr[a])

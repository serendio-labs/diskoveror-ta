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


def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

f=open("newSmiley.txt","r")

lines  = f.readlines()

smiley = dict()

# for line in lines:
# 	line = line.split("\t")
# 	#print (line[0][2:])
# 	smiley[line[0][2:].lower().strip()] = line[1].lower().strip()

for line in lines:
	line = line.lower().strip()
	if line[0] == "#":
		score = float(line[1:len(line)-1])
	splits = line.split()[1:]
	for s in splits:
		smiley[s] = score


print(smiley[":("])
save_obj(smiley,"SmileyDict")

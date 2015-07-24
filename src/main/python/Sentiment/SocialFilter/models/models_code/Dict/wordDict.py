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
import codecs

def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

words = open("words.txt","r")
moreWOrds = codecs.open("finv.txt","r","ISO-8859-1")

w = dict()

for word in words:
	w[word.lower().strip()]=1
for word in moreWOrds:
	w[word.lower().strip()]=1

w.pop("")

#test
print(w["rofl"])


## removing all contactions and abbreviations

acro = load_obj("../../acronymsDict")
conc = load_obj("../../contractionsDict")

remove = list(set(acro.keys()).union(set(conc.keys())))

for r in remove:
	try:
		w.pop(r)
	except:
		continue
save_obj(w,"wordDict")

print(w["hw"])

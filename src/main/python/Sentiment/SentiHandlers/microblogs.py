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
import config
import sys
sys.path.append(config.basePath+config.SocialFilter)
from Sentiment.SocialFilter.TextFilter import Filter
import re

class extractor(object):
	def __init__(self):
		self.path = config.basePath +config.microblog
		self.SentiModel = self.load_obj("_model")
		self.ch2 = self.load_obj("_feature_selector")
		self.vectorizer = self.load_obj("_vectorizer")
		self.filter = Filter()

	def load_obj(self,name):
		    with open( self.path + name + '.pkl', 'rb') as f:
		        return pickle.load(f)

	def getSentimentScore(self,message):
		message = self.filter.process(message)
		emo = re.findall(r"(\w+)#\(([+-]*\d+.\d+)\)#",message.decode("utf-8"))
		emoScore = 0.0
		for e in emo:
			emoScore += float(e[1])
		# print(emo)
				# remove emo info
		onlyEMO = 0
		message = re.sub(r"\w+#\([+-]*\d+.\d+\)#"," ",message.decode("utf-8"))
		if len(message.strip()) == 0:
			onlyEMO = 1
		vec = self.vectorizer.transform([message.encode("utf-8")])
		Tvec = self.ch2.transform(vec)
		predScore = self.SentiModel.predict(Tvec)

		# Vary weights if wanted (default - 0.35,0.65)
		if onlyEMO == 1:
			predScore = emoScore
		elif len(emo) >= 1:
			predScore = 0.35 * predScore + 0.65 * emoScore
		return float(predScore)

# #### TEST
# S = extractor()
# message = ":)"
# print(S.getSentimentScore(message))

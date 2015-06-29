# Author : Satish Palaniappan
__author__ = "Satish Palaniappan"

import pickle
import config
import sys
sys.path.append(config.basePath+config.SocialFilter)
from SocialFilter.Twokenize.twokenize import *
import re

class extractor(object):

	def __init__(self):
		self.path = config.basePath +config.general
		self.SentiModel = self.load_obj("_model")
		self.ch2 = self.load_obj("_feature_selector")
		self.vectorizer = self.load_obj("_vectorizer")

	def load_obj(self,name):
		    with open( self.path + name + '.pkl', 'rb') as f:
		        return pickle.load(f)

	def simpleProcess(self,text):
		text = text.lower().strip()
		line = re.sub(Url_RE,"",text)
		line = re.sub(r"[@#]","",line)
		line =u" ".join(tokenize(line))
		return line

	def getSentimentScore(self,message):
		vec = self.vectorizer.transform([self.simpleProcess(message)])
		Tvec = self.ch2.transform(vec)
		predScore = self.SentiModel.predict(Tvec)
		return float(predScore)

# #### TEST
# S = extractor()
# message = "More Americans Are Renting, and Paying More, as Homeownership Falls"
# print(S.getSentimentScore(message))
# # from scoreScaler import scorer
# # print(scorer().scaleScore(S.getSentimentScore(message)))

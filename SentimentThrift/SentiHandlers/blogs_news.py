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
		self.path = config.basePath +config.microblog
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

	def getSentimentScore(self,title,firstPara,lastPara = "",middleParas = []):

		predScore = 0.0

		# Title (0.40)
		vec = self.vectorizer.transform([self.simpleProcess(title)])
		Tvec = self.ch2.transform(vec)
		predTitleScore = self.SentiModel.predict(Tvec)

		# firstPara (0.25)
		vec = self.vectorizer.transform([self.simpleProcess(firstPara)])
		Tvec = self.ch2.transform(vec)
		predPara1Score = self.SentiModel.predict(Tvec)

		# lastPara (0.25)
		predParaNScore = 0.0
		if lastPara != "":
			vec = self.vectorizer.transform([self.simpleProcess(lastPara)])
			Tvec = self.ch2.transform(vec)
			predParaNScore = self.SentiModel.predict(Tvec)

		# Middle Paragraphs (0.10)
		predParaMiddleScore = 0.0
		for p in middleParas:
			vec = self.vectorizer.transform([self.simpleProcess(p)])
			Tvec = self.ch2.transform(vec)
			predParaMiddleScore += self.SentiModel.predict(Tvec)

		if predParaNScore != 0.0 and predParaMiddleScore != 0.0:
			predScore = 0.60 * predTitleScore + 0.15 * predPara1Score + 0.15 * predParaNScore + 0.10 * predParaMiddleScore
		elif predParaNScore != 0.0 and predParaMiddleScore == 0.0:
			predScore = 0.60 * predTitleScore + 0.20 * predPara1Score + 0.20 * predParaNScore
		else:
			predScore = 0.70 * predTitleScore + 0.30 * predPara1Score

		return float(predScore)

# #### TEST
# S = extractor()
# title = "More Americans Are Renting, and Paying More, as Homeownership Falls"
# firstPara = "TO-DO"
# print(S.getSentimentScore(title,firstPara))

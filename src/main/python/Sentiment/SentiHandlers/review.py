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
from Sentiment.SocialFilter.Twokenize.twokenize import *
import re

class Model(object):
	def __init__(self,modelPath):
		self.path = config.basePath +config.review_Base + modelPath
		self.SentiModel = self.load_obj("_model")
		self.ch2 = self.load_obj("_feature_selector")
		self.vectorizer = self.load_obj("_vectorizer")

	def load_obj(self,name):
		    with open( self.path + name + '.pkl', 'rb') as f:
		        return pickle.load(f)

class extractor(object):
	def __init__(self):
		self.reviewModels = dict()
		for i in config.review.keys():
			if type(config.review[i]) is dict:
				self.reviewModels[i] = dict()
				for k in config.review[i]:
					if config.review[i][k][0] == 1:
						self.reviewModels[i][k] = dict()
						self.reviewModels[i][k]["title"] = Model(config.review[i][k][1])
						self.reviewModels[i][k]["review_text"] = Model(config.review[i][k][2])
					else:
						self.reviewModels[i][k] = Model(config.review[i][k][1])
			else:
				if config.review[i][0] == 1:
					self.reviewModels[i] = dict()
					self.reviewModels[i]["title"] = Model(config.review[i][1])
					self.reviewModels[i]["review_text"] = Model(config.review[i][2])
				else:
					self.reviewModels[i] = Model(config.review[i][1])


	def simpleProcess(self,text):
		text = text.lower().strip()
		line = re.sub(Url_RE,"",text)
		line = re.sub(r"[@#]","",line)
		line =u" ".join(tokenize(line))
		return line

	def getSentimentScore(self,review_text,topDomain,title = "", subDomain = ""):

		# finding the appropriate model
		currentReviewTextModel = ""
		currentReviewTitleModel = ""
		if type(self.reviewModels[topDomain]) is dict:
			try:
				if self.reviewModels[topDomain]["title"]:
					currentReviewTitleModel = self.reviewModels[topDomain]["title"]
					currentReviewTextModel = self.reviewModels[topDomain]["review_text"]
			except:
				if type(self.reviewModels[topDomain][subDomain]) is dict:
					currentReviewTitleModel = self.reviewModels[topDomain][subDomain]["title"]
					currentReviewTextModel = self.reviewModels[topDomain][subDomain]["review_text"]
				else:
					currentReviewTextModel = self.reviewModels[topDomain][subDomain]
		else:
			currentReviewTextModel = self.reviewModels[topDomain]

		# Calculating the sentiments
		predTextScore = 0.0
		predTitleScore = 0.0
		if currentReviewTitleModel == "":
			if title == "":
				vec = currentReviewTextModel.vectorizer.transform([self.simpleProcess(review_text)])
				Tvec = currentReviewTextModel.ch2.transform(vec)
				predTextScore = currentReviewTextModel.SentiModel.predict(Tvec)
			else:
				vec = currentReviewTextModel.vectorizer.transform([self.simpleProcess(review_text)])
				Tvec = currentReviewTextModel.ch2.transform(vec)
				predTextScore = currentReviewTextModel.SentiModel.predict(Tvec)

				vec = currentReviewTextModel.vectorizer.transform([self.simpleProcess(title)])
				Tvec = currentReviewTextModel.ch2.transform(vec)
				predTitleScore = currentReviewTextModel.SentiModel.predict(Tvec)
		else:
			if title == "":
				vec = currentReviewTextModel.vectorizer.transform([self.simpleProcess(review_text)])
				Tvec = currentReviewTextModel.ch2.transform(vec)
				predTextScore = currentReviewTextModel.SentiModel.predict(Tvec)
			else:
				vec = currentReviewTextModel.vectorizer.transform([self.simpleProcess(review_text)])
				Tvec = currentReviewTextModel.ch2.transform(vec)
				predTextScore = currentReviewTextModel.SentiModel.predict(Tvec)

				vec = currentReviewTitleModel.vectorizer.transform([self.simpleProcess(title)])
				Tvec = currentReviewTitleModel.ch2.transform(vec)
				predTitleScore = currentReviewTitleModel.SentiModel.predict(Tvec)

		if predTitleScore != 0.0:
			predScore = 0.7 * predTitleScore + 0.3 * predTextScore
		else:
			predScore = predTextScore

		return float(predScore)

# #### TEST
# S = extractor()
# message = "I have bought and returned three of these units now. Each one has been defective, and finally I just gave up on returning the system. The DVD player constantly gives 'Bad Disc' errors and skips if there is even the slightest smudge on a disc. The sound quality is very nice for the price, but since the player doesn't work, it's essentially useless. This is a complete rip-off at any price point"
# t = "Horrible DVD player"
# print(S.getSentimentScore(message,title = t,topDomain = "electronics_tech",subDomain="cell_phones"))

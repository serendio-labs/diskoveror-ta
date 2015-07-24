# -*- coding: utf-8 -*-
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

### Insert Current Path
import os, sys, inspect
cmd_folder = os.path.realpath(os.path.abspath(os.path.split(inspect.getfile(inspect.currentframe()))[0]))
if cmd_folder not in sys.path:
	sys.path.insert(0, cmd_folder)

import general
import microblogs
import comments
import review
import blogs_news
from scoreScaler import scorer

class SentiHandle(object):

	def __init__(self):
		self.general = general.extractor()
		self.microblogs = microblogs.extractor()
		self.review = review.extractor()
		self.comments = comments.extractor()
		self.blogs_news = blogs_news.extractor()
		self.scalerLargeText = scorer(1.0)
		self.scalerMediumText = scorer(0.5)
		self.scalerSmallText = scorer(1.5)

	def getSentimentScore(self,mainText,textType = "microblogs", title="",middleParas = "",lastPara = "",topDomain="",subDomain=""):
		'''
		Arguments List:

		general - mainText,textType = "general"
		microblogs - mainText, textType = "microblogs"
		comments - mainText, textType = "comments"
		reviews - mainText, textType = "reviews", title = "" <optional>,topDomain,subDomain = "" <depends, not always optional, refer the list in config.py>
		blogs_news - mainText< or first paragraph>, title, textType="blogs_news",lastPara = "" <optional last paragraph>,middleParas = [] <optional middle paragraphs(separate each para with newline into string)>
		'''

		score = 0.0
		scaledScore = 0.0

		if textType == "general":
			score = self.general.getSentimentScore(mainText)
			scaledScore = self.scalerMediumText.scaleScore(score)

		elif textType == "microblogs":
			score = self.microblogs.getSentimentScore(mainText)
			scaledScore = self.scalerSmallText.scaleScore(score)

		elif textType == "comments":
			score = self.comments.getSentimentScore(mainText)
			scaledScore = self.scalerSmallText.scaleScore(score)

		elif textType == "reviews":
			if subDomain == "":
				score = self.review.getSentimentScore(mainText,topDomain,title=title)
				scaledScore = self.scalerLargeText.scaleScore(score)
			else:
				score = self.review.getSentimentScore(mainText,topDomain,title=title,subDomain=subDomain)
				scaledScore = self.scalerLargeText.scaleScore(score)

		elif textType == "blogs_news":
			score = self.blogs_news.getSentimentScore(title,mainText,lastPara,middleParas.split("\n"))
			scaledScore = self.scalerLargeText.scaleScore(score)

		return scaledScore # Integer [-5 to 5]

# '''
# title = "Hefty bill for a defunct borewell"
# firstPara = "A borewell in Vidyaranyapura ward has notched up a whopping bill of Rs. 49,254. The catch here is that the borewell has been defunct for a year now."
# lastPara = "While BBMP Commissioner G. Kumar Naik said he was unaware of the issue, officials of the Bangalore Electricity Supply Company (Bescom), who visited the spot, said they were checking if the borewells belonging to the BBMP in the area had the same RR number. This is to check if the bill pertained to a single one or all of them."
# middleParas = "These allegations were made by the Aam Aadmi Party (AAP), which conducted a social audit recently. Ravi Krishna Reddy of the AAP alleged that there were pending bills amounting to Rs. 1.05 crore for sinking borewells (in three different instalments of Rs. 47 lakh, Rs. 40 lakh and Rs. 20 lakh) in the ward\nWe inspected four borewells, of which only one was used, and that too for just a few months after the power connection was given, he said.\nHe accused the Bruhat Bangalore Manahanagar Palike (BBMP) of sinking borewells unnecessarily to utilise the funds worth Rs. 50 lakh given to each ward to provide drinking water to citizens, which is why many are now defunct.“This is just wastage of public funds. Now, who is going to pay the bill of Rs. 49,254?” he said."
# m = "This is the thinest and sexiest phone on  earth"
# t = "The phone really awesome !!"
# '''
# # # # Test
# handler = SentiHandle()
# print(handler.getSentimentScore("Photo: artpixie: Siempre que puedo lo miro y me inspiroÃ¢ÂÂ¦ agree wit the sologan  http://tumblr.com/xyu1yomro","microblogs","","","","",""))
# # # print(handler.getSentimentScore(textType = "reviews",mainText = "this phone sucks",topDomain = "services",subDomain="other"))
# # # print(handler.getSentimentScore(title = title, textType = "blogs_news",mainText = firstPara,lastPara= lastPara, middleParas = middleParas))

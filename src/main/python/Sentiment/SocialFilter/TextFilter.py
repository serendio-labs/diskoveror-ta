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


import pickle
import re, collections

### Insert Current Path
import os, sys, inspect, traceback
cmd_folder = os.path.realpath(os.path.abspath(os.path.split(inspect.getfile(inspect.currentframe()))[0]))
if cmd_folder not in sys.path:
	sys.path.insert(0, cmd_folder)

from Twokenize import twokenize
from Twokenize import emoticons
#### USAGE   " ".join(twokwnize.tokenize(line))  ## line is the line to be tokenized

from nltk.corpus import stopwords

from nltk.stem import PorterStemmer
stemmer=PorterStemmer()
# USAGE stemmer.stem(String)

# import spell check module
from SpellCheck.Corrector import spellCheck

path = cmd_folder+"/models/"


class Filter(object):

	def __init__(self):

		self.acronyms = self.load_obj("acronymsDict")
		self.emoticons = self.load_obj("SmileyDict")
		self.contractions = self.load_obj("contractionsDict")
		self.wordDict = self.load_obj("wordDict")

		self.spellCheck = spellCheck()

		#Optional Stop Words Removal
		#Amazon nlp stop word list
		self.stop  = ['a', 'across', 'am', 'an', 'and', 'any', 'are', 'as', 'at', 'be', 'been', 'being', 'but', 'by', 'can', 'could', 'did', 'do', 'does', 'each', 'for', 'from', 'had', 'has', 'have', 'in', 'into', 'is', "isn't", 'it', "it'd", "it'll", "it's", 'its', 'of', 'on', 'or', 'that', "that's", 'thats', 'the', 'there', "there's", 'theres', 'these', 'this', 'those', 'to', 'under', 'until', 'up', 'were', 'will', 'with', 'would']
		# self.stop+=[")","(",".","'",",",";",":","?","/","!","@","$","*","+","-","_","=","&","%","`","~","\"","{","}"]

	def load_obj(self,name ):
	    with open( path + name + '.pkl', 'rb') as f:
	        return pickle.load(f)

	def process(self,text,stopwordsF = 0, stemmerF = 0, encode = 1):

		# remove URL
		line = re.sub(twokenize.Url_RE," ", text)

		# to strip of extra white spaces
		temp = line.replace("#" , " ").lower().split()
		temp = " ".join(temp)

		tempTweet = ""

		for word in twokenize.tokenize(temp):
			if word != " ":

				word = word.strip()
				flagNonDict = 0

				try:
					#### Check Dict and set flag
					if self.wordDict[word] == 1:
						# print(word)
						word =	word
				except:
					flagNonDict = 1
					try:
						score = self.emoticons[word]
						emo = emoticons.analyze_tweetHeavy(word)
						word = emo + "#("+ str(score) +")#"
						# print(word)
					except:
						try:
							#Normalize Acronyms
							word = self.acronyms[word]
						except:
							try:
								#Normalize Contractions
								word = self.contractions[word]
							except:
								#Normalize words (Spell)
								if flagNonDict == 1:
									if "@" in word:
										# remove user mentions
										word = "@user"
									else:
										corrected = self.spellCheck.correct(word)
										if corrected != "a":
											word = corrected
				try:
					tempTweet = " ".join([tempTweet,word.strip()])
					tempTweet = tempTweet.lower().strip()
				except:
					tempTweet = " ".join([tempTweet,word.strip().decode("iso-8859-1")])
					tempTweet = tempTweet.lower().strip()

		if stemmerF == 1 and stopwordsF == 1:
			tempTweet = " ".join(stemmer.stem(w) for w in tempTweet.split(" ") if w not in self.stop)
		elif stemmerF == 1:
			tempTweet = " ".join(stemmer.stem(w.strip()) for w in tempTweet.split(" "))
		elif stopwordsF == 1:
			tempTweet = " ".join(w for w in tempTweet.split(" ") if w.strip() not in self.stop)

		# print(tempTweet.encode("utf-8"))
		if encode == 0:
			return(tempTweet)
		return(tempTweet.encode("utf-8"))

# #Usage
# filter = Filter()
# filter.process("god am having ampple amount of time pls help me with my hw :) :(:D#lol ")

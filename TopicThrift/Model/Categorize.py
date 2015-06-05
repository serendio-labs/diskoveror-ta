import re,sys
from collections import defaultdict
import csv
from rake import rake
from twokenize import *
from nltk.corpus import stopwords

from Kseeds import mcategorizerClass
from Seeds22 import categorizerClass

path = "./Model/"

class Categorize(object):

	def __init__(self):

		# Create Classes
		self.catz = mcategorizerClass.Categorize()
		self.cats = categorizerClass.Categorize()
		# Init Prep List
		self.Code  = r"\\[a-zA-Z0-9]+"
		self.ReList = [
		    Url_RE,
		    Entity,
		    Timelike,
		    NumNum,
		    NumberWithCommas,
		    self.Code,
		    Punct,
		    Separators,
		    Decorations
		]
		self.stoplistw = stopwords.words('english')
		self.stoplist = [")","(",".","'",",",";",":","?","/","!","@","$","*","+","-","_","=","&","%","`","~","\"","{","}"]
		#Load Rake
		self.rake = rake.Rake(path + "/rake/SmartStoplist.txt")

	def prep (self,text):
		line = text
		for r in ["@","#"]:
			line = line.lower().replace(r," ")
		for w in self.stoplistw:
			line = line.replace(" "+ w +" "," ")
		for w in self.stoplist:
			line = line.replace(w," ")
		return line

	def aggresivePreproces (self,text):
		line = text.decode("utf-8")
		for ree in self.ReList:
			line = re.sub(ree,"", str(line.strip()))
		return line

	def getCategory(self,text):
		text = str(text.strip()).encode("utf-8")
		# comment if too bad results
		text = self.aggresivePreproces(text)
		text = self.prep(text)
		TPtext = tokenize(text)

		twokCatz = self.catz.getCategory(TPtext)
		stwokCatz = self.cats.getCategory(TPtext)

		RPText = self.rake.run(u" ".join(TPtext).lower().strip())

		Words = []
		for pt in RPText:
			pt=pt[0]
			if len(pt.split()) > 1:
				Words.extend(pt.split())
			else:
				Words.extend([pt])
		wordCatz = self.catz.getCategory(Words)
		swordCatz = self.cats.getCategory(Words)

		catD = defaultdict(int)

		# Bagging
		for i in [twokCatz,stwokCatz,wordCatz,swordCatz]:
			if i == "general":
				catD["general"] += 1
			else:
				for cat in i[0].split("|"):
					catD[cat] += 1
		baggedCatz = []
		maxCat = 4
		for k in catD.keys():
			if catD[k] >= 2:
				baggedCatz.append(k)
		if len(baggedCatz) > maxCat:
			baggedCatz=[]
			for k in catD.keys():
				if catD[k] >= 3:
					baggedCatz.append(k)
		if len(baggedCatz) > maxCat:
			baggedCatz=[]
			for k in catD.keys():
				if catD[k] >= 4:
					baggedCatz.append(k)
		if len(baggedCatz) == 0:
			if swordCatz == "general":
				baggedCatz = ["general"]
			else:
				baggedCatz=swordCatz[0].split("|")

		return ("|".join(baggedCatz))

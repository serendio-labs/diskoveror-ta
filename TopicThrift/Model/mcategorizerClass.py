import pickle
from rake import rake
import twokenize
import gensim
from scipy import spatial
import operator
import numpy as np

path = "./Model/"

def save_obj(obj, name ):
    with open(path + name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name):
    with open( path + name + '.pkl', 'rb') as f:
        return pickle.load(f)

class Categorize(object):
	
	def __init__(self):

		## Load Pickle
		self.Cluster_lookUP = load_obj("Cluster_lookUP")
		self.Cosine_Similarity = load_obj("Cosine_Similarity")
		self.num2cat = load_obj("num2cat")
		self.Cluster_Model = load_obj("clusterLarge")
		self.catVec = load_obj("catVec")
		self.numK2CatMap = load_obj("numK2CatMap")
		# the following part is needed to process phrases
		self.model = gensim.models.Word2Vec.load_word2vec_format(path + 'vectors.bin', binary=True)
		self.model.init_sims(replace=True)
		#Load Rake
		self.rake = rake.Rake("SmartStoplist.txt")

	def CosSim (self,v1,v2):
		return (1 - spatial.distance.cosine(v1, v2))

	def combine(self,v1,v2):
		A = np.add(v1,v2)
		M = np.multiply(A,A)
		lent=0
		for i in M:
			lent+=i
		return np.divide(A,lent)

	def getCategory(self,text):
		# Text To Categorize = text

		# Plus Rake gets phrases that mean different and finds vecs to get cat
		# Ptext = u" ".join(twokenize.tokenize(text))
		# Ptext = self.rake.run(Ptext.lower().strip())
		
		# Just Twokenize gives more number of cats (very noisy)
		#Ptext = twokenize.tokenize(text)

		# Twok and Rake and get Keywords not phrases
		Ptext = u" ".join(twokenize.tokenize(text))
		Ptext = self.rake.run(Ptext.lower().strip())

		Words = []
		for pt in Ptext:
			pt=pt[0]
			if len(pt.split()) > 1:
				Words.extend(pt.split())
			else:
				Words.extend([pt])

		scores=dict()
		for i in range(0,22):
			scores[i] = 0.0

		for phrase in Words:
			# phrase = phrase[0]
			if len(phrase.split()) == 1:
				try:
					if len(self.Cluster_lookUP[phrase]) == 1:
						scores[self.Cluster_lookUP[phrase][0]] += self.Cosine_Similarity[phrase]
					else:
						for kw in self.Cluster_lookUP[phrase]:
							scores[kw] += self.Cosine_Similarity[phrase]
						#print(num2cat[Cluster_lookUP[phrase]])
				except:
					#try:	## for new 3.5 gb model file alone
					#	vec = model[phrase]
					#	tC = self.Cluster_Model.predict(vec)
					#	# tempcat returns K index we need to map it to 22 topics
					#	scores[self.numK2CatMap[tC[0]]] += CosSim(vec,self.catVec[tC[0]])
					#	continue
					#except:
					continue
			# else:
			# 	return("Break")
			# the following part is needed to process phrases (if not needed uncomment above two lines and comment the below else part)
			else:
				words = phrase.split()
				try:
					vec = np.array(model[words[0]])
					for word in words[1:]:
						try:
							vec = combine(vec,np.array(model[word]))
						except:
							#print(word + " Skipped!")
							continue
					tempCat = self.Cluster_Model.predict(vec)
					#print(num2cat[tempCat[0]])
					# tempcat returns K index we need to map it to 22 topics
					scores[self.numK2CatMap[tempCat[0]]] += CosSim(vec,self.catVec[tempCat[0]])
				except:
					#print(words[0] + " Skipped!")
					continue
		#print(scores)

		'''Modifiable Parameters'''
		# Vary this value to tune models Multi Topic Performance
		thresholdP = 0.35  # This value is in percent
		# if u want a more finer prediction set threshold to 0.35 or 0.40 (caution: don't exceed 0.40)
		maxS = max(scores.items(), key = operator.itemgetter(1))[1] 
		threshold = maxS * 0.30
		
		#Min Score
		minScore = 0.30 
		# if u want a more noise free prediction set threshold to 0.35 or 0.40 (caution: don't exceed 0.40)
		flag = 0
		if maxS < minScore:
			flag = 1
		# set max number of cats assignable to any text
		catLimit = 4  # change to 3 or less more aggresive model
		# more less the value more aggresive the model 

		scoreSort  = sorted(scores.items(), key = operator.itemgetter(1), reverse=True)
		#print(scoreSort)
		cats = []
		f=0
		for s in scoreSort:
			if s[1] != 0.0 and s[1] > threshold:
				f=1
				cats.extend([self.num2cat[s[0]]])		
			else:
				continue
		if f == 0 or flag == 1: #No Category assigned!
			return ("general")
		else:
			if len(cats) == 1:
				ret = str(cats[0])
			elif len(cats) <= catLimit:
				ret = "|".join(cats)
			else:
				# ret = "general" or return top most topic
				ret = cats[0] +"|"+"general"
			return ret

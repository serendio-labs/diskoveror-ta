#import Preprocess.PreprocessClass as PC
from process import *
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.svm import LinearSVC
import pickle

path = "./Model/"

def load_obj(name ):
	    with open( path + name + '.pkl', 'rb') as f:
	        return pickle.load(f)

class Senti(object):
	def __init__(self):
		#self.P = PC.Preprocess()
		self.SentiModel = load_obj("SentiModel")
		self.ch2 = load_obj("Chi2Model")
		self.vectorizer = load_obj("TfidfVectorizer")

	def getSentiment(self,message):
		# Special PreProcess
		# message = P.process(message)
		# Simple PreProcess
		message = process(message)
		vec = self.vectorizer.transform([message])
		Tvec = self.ch2.transform(vec)
		pred = self.SentiModel.predict(Tvec)
		return float(pred[0])

##### TEST
# message ="i am in love"
# S = Senti()
# print(S.getSentiment(message))

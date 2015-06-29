# Author : Satish Palaniappan
__author__ = "Satish Palaniappan"
# Scoring Function

class scorer(object):
	def __init__(self,maxmin = 1.5):
		self.maxmin = maxmin
		self.buckets = [self.maxmin - i*(self.maxmin*2/11) for i in range(0,12)]

	def scaleScore (self,x):
		for i in range(0,11):
			if x > self.buckets[0]:
				return 5
			if x < self.buckets[len(self.buckets)-1]:
				return -5
			if self.buckets[i] >= x >= self.buckets[i+1]:
				return(5-i)

# print(getScore(-0.2335241799033111))

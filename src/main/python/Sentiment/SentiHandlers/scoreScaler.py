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

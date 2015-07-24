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


import numpy as np

from sklearn import cluster, datasets, preprocessing
import pickle
import gensim
import time
import re
import tokenize
from scipy import spatial

def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)


# 3M word google dataset of pretrained 300D vectors
model = gensim.models.Word2Vec.load_word2vec_format('vectors.bin', binary=True)
model.init_sims(replace=True)

#### getting all vecs from w2v using the inbuilt syn0 list see code

# X = model.syn0

# ### scaling feature vecs
# min_max_scaler = preprocessing.MinMaxScaler()
# X_Scaled_Feature_Vecs = min_max_scaler.fit_transform(X)

# X_Scaled_Feature_Vecs = X

# W2V = dict(zip(model.vocab, X_Scaled_Feature_Vecs))

#Cosine Distance
# from scipy import spatial

# dataSetI = model["travel"]
# dataSetII = model["travelling"]
# result = 1 - spatial.distance.cosine(dataSetI, dataSetII)
# print(result)

X_Scaled_Feature_Vecs=[]
for word in model.vocab:
    X_Scaled_Feature_Vecs.append(model[word])

# ######## Interested Categories
cat = [
"advertising",
"beauty",
"business",
"celebrity",
"diy craft",
"entertainment",
"family",
"fashion",
"food",
"general",
"health",
"lifestyle",
"music",
"news",
"pop",
"culture",
"social",
"media",
"sports",
"technology",
"travel",
"video games"
]

nums = range(0,22)
num2cat = dict(zip(nums, cat))

catVec=[]
# load from C file output
for c in cat:
    try:
        catVec.append(model[c.lower()])
    except:
        words = c.split()
        A = np.add(np.array(model[words[0].lower()]),np.array(model[words[1].lower()]))
        M = np.multiply(A,A)
        lent=0
        for i in M:
            lent+=i
        V = np.divide(A,lent)
        catVec.append(list(V))
# kmeans
##### better code
t0 = time.time()
# Assign Max_Iter to 1 (ONE) if u just want to fit vectors around seeds
kmeans = cluster.KMeans(n_clusters=22, init=np.array(catVec), max_iter=1).fit(X_Scaled_Feature_Vecs)
#kmeans = cluster.KMeans(n_clusters=22, init=np.array(catVec), max_iter=900).fit(X_Scaled_Feature_Vecs)
print(str(time.time()-t0))
print(kmeans.inertia_)


###### After Fiting the Cluster Centers are recomputed : update catVec (Order Preserved)
catVec = kmeans.cluster_centers_

# #test
# for c in catVec:
#     print(num2cat[kmeans.predict(c)[0]])

##### save best for future use
save_obj(kmeans,"clusterSmall")
KM = load_obj("clusterSmall")
# Cluster_lookUP = dict(zip(model.vocab, KM.labels_))
Cluster_lookUP = dict()
for word in model.vocab:
    Cluster_lookUP[word] = KM.predict(model[word])[0]

## Precomputing the cosine similarities

Cosine_Similarity = dict()
for k in Cluster_lookUP.keys():
    Cosine_Similarity[k] = 1 - spatial.distance.cosine(model[k], catVec[Cluster_lookUP[k]])

#check
print(num2cat[Cluster_lookUP["flight"]] + "   "+str(Cosine_Similarity["flight"]))
print(num2cat[Cluster_lookUP["gamecube"]] +"   "+str(Cosine_Similarity["gamecube"]))

#Saving Models
save_obj(Cluster_lookUP,"Cluster_lookUP")
save_obj(Cosine_Similarity,"Cosine_Similarity")
save_obj(num2cat,"num2cat")
save_obj(catVec,"catVec")

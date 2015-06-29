import pickle
import codecs

def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

words = open("words.txt","r")
moreWOrds = codecs.open("finv.txt","r","ISO-8859-1")

w = dict()

for word in words:
	w[word.lower().strip()]=1
for word in moreWOrds:
	w[word.lower().strip()]=1

w.pop("")

#test
print(w["rofl"])


## removing all contactions and abbreviations

acro = load_obj("../../acronymsDict")
conc = load_obj("../../contractionsDict")

remove = list(set(acro.keys()).union(set(conc.keys())))

for r in remove:
	try:
		w.pop(r)
	except:
		continue
save_obj(w,"wordDict")

print(w["hw"])

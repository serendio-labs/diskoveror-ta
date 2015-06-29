import pickle

def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

acro = load_obj("acronymsDict")

# Spit out
# for a in acro.keys():
# 	print(a + " : " + acro[a])

acroLines = open("acroClean.txt","r").readlines()

acronymsDict = dict()

for a in acroLines:
	k,v = a.split(" : ")
	k,v = k.strip().lower(),v.strip().lower()
	acronymsDict[k] = v

print(len(acronymsDict))
save_obj(acronymsDict,"acronymsDict")

print(acronymsDict["plz"])

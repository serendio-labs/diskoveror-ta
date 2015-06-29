import pickle


def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)


con  = open("Contractions.txt","r")

lines = con.readlines()

cocn = dict()

for line in lines:
	line = line.strip()
	parts = line.split("\t")
	cocn[parts[0].lower().strip()]=parts[1].lower().strip()

save_obj(cocn,"contractionsDict")

#test
print(cocn["you'd've"])

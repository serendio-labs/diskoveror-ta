import pickle


def save_obj(obj, name ):
    with open( name + '.pkl', 'wb') as f:
        pickle.dump(obj, f,  protocol=2)

def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

f=open("newSmiley.txt","r")

lines  = f.readlines()

smiley = dict()

# for line in lines:
# 	line = line.split("\t")
# 	#print (line[0][2:])
# 	smiley[line[0][2:].lower().strip()] = line[1].lower().strip()

for line in lines:
	line = line.lower().strip()
	if line[0] == "#":
		score = float(line[1:len(line)-1])
	splits = line.split()[1:]
	for s in splits:
		smiley[s] = score


print(smiley[":("])
save_obj(smiley,"SmileyDict")

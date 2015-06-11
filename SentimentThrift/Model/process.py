from twokenize import *

Code  = r"\\[a-zA-Z0-9]+"
ReList = [
    Url_RE,
    Entity,
    Timelike,
    NumNum,
    NumberWithCommas,
    Code,
    Punct,
    Separators,
    Decorations
]

stoplist = [")","(",".","'",",",";",":","?","/","!","@","$","*","+","-","_","=","&","%","`","~","\"","{","}"]


def prep (text):
	line = text
	for r in ["@","#"]:
		line = line.lower().replace(r," ")
	for w in stoplist:
		line = line.replace(w," ")
	return line

def aggresivePreproces (text):
	line = text.decode("utf-8")
	for ree in ReList:
		line = re.sub(ree,"", str(line.strip()))
	return line
def process(text):
	text = str(text.strip()).encode("utf-8")
	text = aggresivePreproces(text)
	text = prep(text)
	text =u" ".join(tokenize(text))
	return text.decode("utf-8")

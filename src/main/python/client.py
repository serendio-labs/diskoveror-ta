# -*- coding: utf-8 -*-
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


from ThriftWrapper import PyInterface
from ThriftWrapper.ttypes import *
from ThriftWrapper.constants import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

import csv
import pickle


def load_obj(name ):
    with open( name + '.pkl', 'rb') as f:
        return pickle.load(f)

'''
Arguments List:

general -> mainText,textType = "general"
microblogs -> mainText, textType = "microblogs"
comments -> mainText, textType = "comments"
reviews -> mainText, textType = "reviews", title = "" <optional>,topDomain,subDomain = "" <depends, not always optional, refer the list in config.py>
blogs_news -> mainText< or first paragraph>, title, textType="blogs_news",lastPara = "" <optional last paragraph>,middleParas = [] <optional middle paragraphs(separate each para with newline into string)>
'''

try:
    # Make socket
    transport = TSocket.TSocket('localhost', 8002)
    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TBufferedTransport(transport)
    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    # Create a client to use the protocol encoder
    client = PyInterface.Client(protocol)
    # Connect!
    transport.open()
    client.ping()    	    
    text = 'The Union government on Thursday proposed to strip the Reserve Bank Governors veto vote on Indias monetary policy. The government also proposed to grant itself the power to appoint four of the six members of the Monetary Policy Committee, whose remit will include decisions on setting interest rates to maintain inflation at the targeted level. The revised draft of the Indian Financial Code, put out by the Union Finance Ministry for comments, proposes that the Reserve Bank Chairperson shall head the committee, with no reference to the Governor. It is not clear from the draft if a re-designation is planned. An earlier draft had proposed to give the Governor the right to overrule the monetary policy committee decision. If the inflation target is not met, then the Reserve Bank will have to explain the reasons and propose remedial actions. Under the revised draft, the non-government members of the committee are to be drawn from the Reserve Bank. The Reserve Banks Board will nominate one of its executives as the fifth member of the committee. The Chairperson will nominate one of its employees as the sixth member. The move comes in the wake of a severe breakdown of talks between the Centre and the Reserve Bank over amendments to the RBI Act, which Finance Minister Arun Jaitley had announced in his Budget speech.'
    text1 = text
    text = text.encode("utf-8")
    obj  = SentiRequestObject(text)
    senti = client.getSentimentScore(obj)
    topics = client.getTopics(text)
    keywords = client.getKeywords(text)

    print keywords
    print topics
    print("The Text: "+ text)
    print(senti)

except Thrift.TException as tx:
    print ("%s" % (tx.message))
    '''
	writer = csv.writer(open("output.csv","w"))

	raw = load_obj("tweetDump")
	# raw = open("dataMultiSources.txt","r").readlines()
	for text in raw:
		# if "##" in text:
		# 	writer.writerow([text.replace("##","")," "])
		# else:
		text = text.strip()
		text = text.encode("utf-8")
		obj  = SentiRequestObject(text)
		senti = client.getSentimentScore(obj)
		print("The Text: "+ text)
		print(senti)
		writer.writerow([str(text),str(senti)])

	transport.close()
	'''


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
#from atk import Text


from IPython.core.release import keywords
__author__ = "Satish Palaniappan, Praveen Jesudhas"


import sys,traceback
sys.path.append("../")

from ThriftWrapper import PyInterface
from Sentiment.SentiHandlers.SentiMaster import SentiHandle
from Topics import Categorize

from ThriftWrapper.ttypes import *
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import socket


class PyInterfaceServer:
    def __init__(self):
        self.log = {}
        self.S = SentiHandle()
        self.catz = Categorize.Categorize()

    def ping(self):
        print ("Ping Success !! :D")
        return

    def getSentimentScore(self, obj):
       
        '''
        Arguments List:
        general -> mainText,textType = "general"
        microblogs -> mainText, textType = "microblogs"
        comments -> mainText, textType = "comments"
        reviews -> mainText, textType = "reviews", title = "" <optional>,topDomain,subDomain = "" <depends, not always optional, refer the list in config.py>
        blogs_news -> mainText< or first paragraph>, title, textType="blogs_news",lastPara = "" <optional last paragraph>,middleParas = [] <optional middle paragraphs(separate each para with newline into string)>
        '''
        try:
            S = self.S.getSentimentScore(obj.mainText,obj.textType,obj.title,obj.middleParas,obj.lastPara,obj.topDomain,obj.subDomain)
            print ("Sentiment Text : " + obj.mainText + " ||| SentimentScore[-5 to 5]: " + str(S))
            return S
        except Exception as err:
            print(traceback.format_exc())
            print(sys.exc_info()[0])
            print err

    def getTopics(self, text):
        text=text.decode('utf-8')
        print text
        cat = self.catz.getCategory(text)
        print ("Topic Text : " + text + " ||| Topic: " + cat)
        res = cat.split('|')
        return res
   
    def getKeywords(self, text):
        text=text.decode('utf-8')
        print "Keyword Text",text
        keywords = self.catz.getKeywords(text)
        return keywords 
         

#port = '8002'
handler = PyInterfaceServer()
processor = PyInterface.Processor(handler)
transport = TSocket.TServerSocket(port=8002)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print "Python Interface server for Sentiment, Topics and Keywords started ..."
server.serve()

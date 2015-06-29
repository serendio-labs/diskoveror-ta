# -*- coding: utf-8 -*-
# Author : Satish Palaniappan
__author__ = "Satish Palaniappan"

'''
Copyright 2015 Satish Palaniappan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
'''

import sys,traceback

from sentiments import Sentiments
from sentiments.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import socket

sys.path.append("../")
from SentiHandlers.SentiMaster import SentiHandle

class SentimentHandler:
  def __init__(self):
    self.log = {}
    self.S = SentiHandle()

  def ping(self):
    print ("Ping Success !! :D")
    return

  def getSentimentScore(self,obj):
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
      print ("The Text : " + obj.mainText + " ||| SentimentScore[-5 to 5]: " + str(S))
      return S
    except Exception as err:
          print(traceback.format_exc())
          print(sys.exc_info()[0])

handler = SentimentHandler()
processor = Sentiments.Processor(handler)
transport = TSocket.TServerSocket(port=8002)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print ("Python sentiment server running...")
server.serve()

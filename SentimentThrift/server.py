import sys

sys.path.append('./gen-py')
sys.path.append('./gen-py/helloworld')
from sentiments import Sentiments
from sentiments.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import socket

sys.path.append("./Model/")
import SentiAPI


class SentimentHandler:
  def __init__(self):
    self.log = {}
    self.S = SentiAPI.Senti()

  def ping(self):
    print ("Ping Success !! :)")
    return

  def getSentiment(self, text):
    S = self.S.getSentiment(text)
    print ("The Text : " + text + " ||| Sentiment: " + str(S))
    return S

handler = SentimentHandler()
processor = Sentiments.Processor(handler)
transport = TSocket.TServerSocket(port=8002)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print ("Starting python server...")
server.serve()

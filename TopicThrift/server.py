import sys

sys.path.append('./gen-py')
sys.path.append('./gen-py/helloworld')
from categorizer import Categorizer
from categorizer.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import socket

sys.path.append("./Model/")
import Categorize


class CategorizerHandler:
  def __init__(self):
    self.log = {}
    self.catz = Categorize.Categorize()
  def ping(self):
    print ("Ping Success !! :)")
    return

  def getTopic(self, text):
    cat = self.catz.getCategory(text)
    print ("The Text : " + text + " ||| Topic: " + cat)
    return cat

handler = CategorizerHandler()
processor = Categorizer.Processor(handler)
transport = TSocket.TServerSocket(port=8001)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print ("Starting python server...")
server.serve()

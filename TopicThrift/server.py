### Insert Current Path
import os, sys, inspect
cmd_folder = os.path.realpath(os.path.abspath(os.path.split(inspect.getfile(inspect.currentframe()))[0]))
if cmd_folder not in sys.path:
  sys.path.insert(0, cmd_folder)

sys.path.append(cmd_folder + '/gen-py')
from categorizer import Categorizer
from categorizer.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import socket

sys.path.append(cmd_folder + "/Model/")
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

print ("Python topics server running...")
server.serve()

import sys
sys.path.append('./gen-py')
sys.path.append('./gen-py/helloworld')
sys.path.append("/media/satish/Root/Users/$@T!$#/Downloads/Official/Projects/ML Projects/Serendio/Topics/Topic-Modeing-Social-Network-Text-Data/K seeds/")

from categorizer import Categorizer
from categorizer.ttypes import *
from categorizer.constants import *
 
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
 
try:
  # Make socket
  transport = TSocket.TSocket('localhost', 8001)
 
  # Buffering is critical. Raw sockets are very slow
  transport = TTransport.TBufferedTransport(transport)
 
  # Wrap in a protocol
  protocol = TBinaryProtocol.TBinaryProtocol(transport)
 
  # Create a client to use the protocol encoder
  client = Categorizer.Client(protocol)
 
  # Connect!
  transport.open()
 
  client.ping()
  print ("ping()")

  text = input("Enter Text to Categorize: ")
  cat = client.getTopic(text)
  print (cat)
      
  transport.close()
 
except Thrift.TException as tx:
  print ("%s" % (tx.message))


import sys
sys.path.append('./gen-py')
sys.path.append('./gen-py/helloworld')
sys.path.append("../")

from sentiments import Sentiments
from sentiments.ttypes import *
from sentiments.constants import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

# try:
# Make socket
transport = TSocket.TSocket('localhost', 8002)

# Buffering is critical. Raw sockets are very slow
transport = TTransport.TBufferedTransport(transport)

# Wrap in a protocol
protocol = TBinaryProtocol.TBinaryProtocol(transport)

# Create a client to use the protocol encoder
client = Sentiments.Client(protocol)

# Connect!
transport.open()

client.ping()
print ("ping()")

text = input("Enter Text to Sentiment: ")
S = client.getSentiment(text)
print (S)

transport.close()

# except Thrift.TException as tx:
#   print ("%s" % (tx.message))

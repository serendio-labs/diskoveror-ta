'''
Copyright 2015 Serendio Inc.
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
'''
__author__ = "Satish Palaniappan"

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


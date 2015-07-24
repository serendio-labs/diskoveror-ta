__author__ = "Brendan O'Connor (anyall.org, brenocon@gmail.com)"
'''
Copyright 2015 Serendio Inc.
Modified By - Satish Palaniappan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
'''

### Insert Current Path
import os, sys, inspect
cmd_folder = os.path.realpath(os.path.abspath(os.path.split(inspect.getfile(inspect.currentframe()))[0]))
if cmd_folder not in sys.path:
  sys.path.insert(0, cmd_folder)

import re,sys

mycompile = lambda pat:  re.compile(pat,  re.UNICODE)
#SMILEY = mycompile(r'[:=].{0,1}[\)dpD]')
#MULTITOK_SMILEY = mycompile(r' : [\)dp]')

NormalEyes = r'[:=]'
Wink = r'[;]'

NoseArea = r'(|o|O|-)'   ## rather tight precision, \S might be reasonable...

HappyMouths = r'[D\)\]d]'
SadMouths = r'[\(\[]'
Tongue = r'[pP]'
OtherMouths = r'[oO/\\]'  # remove forward slash if http://'s aren't cleaned

Happy_RE =  mycompile( '(\^_\^|' + NormalEyes + NoseArea + HappyMouths + ')')
Sad_RE = mycompile(NormalEyes + NoseArea + SadMouths)

Wink_RE = mycompile(Wink + NoseArea + HappyMouths)
Tongue_RE = mycompile(NormalEyes + NoseArea + Tongue)
Other_RE = mycompile( '('+NormalEyes+'|'+Wink+')'  + NoseArea + OtherMouths )

Emoticon = (
    "("+NormalEyes+"|"+Wink+")" +
    NoseArea +
    "("+Tongue+"|"+OtherMouths+"|"+SadMouths+"|"+HappyMouths+")"
)
Emoticon_RE = mycompile(Emoticon)

#Emoticon_RE = "|".join([Happy_RE,Sad_RE,Wink_RE,Tongue_RE,Other_RE])
#Emoticon_RE = mycompile(Emoticon_RE)

def analyze_tweet(text):
  h= Happy_RE.search(text)
  s= Sad_RE.search(text)
  if h and s: return "neutral"
  if h: return "happy"
  if s: return "sad"
  return "nill"

# more complex & harder
def analyze_tweetHeavy(text):
  h= Happy_RE.search(text)
  s= Sad_RE.search(text)
  w= Wink_RE.search(text)
  t= Tongue_RE.search(text)
  a= Other_RE.search(text)
  h,w,s,t,a = [bool(x) for x in [h,w,s,t,a]]
  if sum([h,w,s,t,a])>1: return "neutral"
  if sum([h,w,s,t,a])==1:
   if h: return "happy"
   if s: return "sad"
   if w: return "happy"
   if a: return "other"
   if t: return "tongue"
  return "nill"

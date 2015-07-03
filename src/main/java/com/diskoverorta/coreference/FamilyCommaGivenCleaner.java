/*******************************************************************************
 *   Copyright 2015 Serendio Inc. ( http://www.serendio.com/ )
 *   Author - Praveen Jesudhas
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/

package com.diskoverorta.coreference;


/**
 * <b>Experimental</b> cleaner for person names of the form "Smith,
 * John".  Based on the PersonNameCleaner. It also normalizes periods
 * in initials, so that "J.R. Ackerley" becomes "J. R. Ackerley".
 */
public class FamilyCommaGivenCleaner implements Cleaner {
  private PersonNameCleaner sub;

  public FamilyCommaGivenCleaner() {
    this.sub = new PersonNameCleaner();
  }

  public String clean(String value) {
    int i = value.indexOf(',');
    if (i != -1)
      value = value.substring(i + 1) + " " + value.substring(0, i);

    char[] tmp = new char[value.length() * 2];
    int pos = 0;
    for (int ix = 0; ix < value.length(); ix++) {
      tmp[pos++] = value.charAt(ix);
      if (value.charAt(ix) == '.' &&
          ix+1 < value.length() &&
          value.charAt(ix + 1) != ' ')
        tmp[pos++] = ' ';
    }

    return sub.clean(new String(tmp, 0, pos));
  }
}
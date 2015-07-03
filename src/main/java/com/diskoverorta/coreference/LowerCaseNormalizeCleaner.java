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

import java.text.Normalizer;



/**
 * A cleaner which removes leading and trailing whitespace, normalized
 * internal whitespace, lowercases all characters, and (by default)
 * strips accents. This is the most commonly used cleaner for textual
 * data.
 */
public class LowerCaseNormalizeCleaner implements Cleaner {
  private boolean strip_accents = true;

  /**
   * Controls whether accents are stripped (that is, "é" becomes "e",
   * and so on). The default is true.
   */
  public void setStripAccents(boolean strip_accents) {
    this.strip_accents = strip_accents;
  }
  
  public String clean(String value) {
    if (strip_accents) 
      // after this, accents will be represented as separate combining
      // accent characters trailing the character they belong with. the
      // next step will strip them out.
      value = Normalizer.normalize(value, Normalizer.Form.NFD);
    
    char[] tmp = new char[value.length()];
    int pos = 0;
    boolean prevws = false;
    for (int ix = 0; ix < tmp.length; ix++) {
      char ch = value.charAt(ix);

      // we make an exception for \u030A (combining ring above) when
      // following 'a', because this is a Scandinavian character that
      // should *not* be normalized
      if (ch == 0x030A && (value.charAt(ix - 1) == 'a' ||
                           value.charAt(ix - 1) == 'A')) {
        prevws = false;
        // this overwrites the previously written 'a' with 'aa'
        tmp[pos - 1] = '\u00E5';
        continue;
      }

      // if character is combining diacritical mark, skip it.
      if ((ch >= 0x0300 && ch <= 0x036F) ||
          (ch >= 0x1DC0 && ch <= 0x1DFF) ||
          (ch >= 0x20D0 && ch <= 0x20FF) ||
          (ch >= 0xFE20 && ch <= 0xFE2F))
        continue;

      // whitespace processing
      if (ch != ' ' && ch != '\t' && ch != '\n' && ch != '\r' &&
          ch != 0xA0 /* NBSP */) {
        if (prevws && pos != 0)
          tmp[pos++] = ' ';

        tmp[pos++] = Character.toLowerCase(ch);
        
        prevws = false;
      } else
        prevws = true;
    }
    return new String(tmp, 0, pos);
  }
}

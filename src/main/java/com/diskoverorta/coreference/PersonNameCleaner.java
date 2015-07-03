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

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;




/**
 * <b>Experimental</b> cleaner for person names, which understands
 * about abbreviations like "joe" for "joseph", etc.
 */
public class PersonNameCleaner implements com.diskoverorta.coreference.Cleaner {
  private LowerCaseNormalizeCleaner sub;
  private Map<String, String> mapping;

  public PersonNameCleaner() {
    this.sub = new LowerCaseNormalizeCleaner();

    // load token translation mapping (FIXME: move to static init?)
    try {
      this.mapping = loadMapping();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String clean(String value) {
    // do basic cleaning
    value = sub.clean(value);
    if (value == null || value.equals(""))
      return value;

    // tokenize, then map tokens, then rejoin
    String[] tokens = StringUtils.split(value);
    for (int ix = 0; ix < tokens.length; ix++) {
      String mapsto = mapping.get(tokens[ix]);
      if (mapsto != null)
        tokens[ix] = mapsto;
    }
    
    return StringUtils.join(tokens);
  }

  private Map<String, String> loadMapping() throws IOException {
  //  String mapfile = "com/diskoverorta/coreference/name-mappings.txt";
	  String mapfile = "name-mappings.txt";
    Map<String, String> mapping = new HashMap();
    ClassLoader cloader = Thread.currentThread().getContextClassLoader();
  /*  InputStream istream = cloader.getResourceAsStream(mapfile);
    InputStreamReader reader = new InputStreamReader(istream, "utf-8");
    BufferedReader in = new BufferedReader(reader);
*/
    BufferedReader in = new BufferedReader(new FileReader(new File(mapfile)));
    
    String line = in.readLine();
    while (line != null) {
      int pos = line.indexOf(',');
      mapping.put(line.substring(0, pos), line.substring(pos + 1));
      line = in.readLine();
    }

    in.close();
    return mapping;
  }
}
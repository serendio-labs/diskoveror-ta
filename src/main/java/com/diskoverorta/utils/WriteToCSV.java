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
package com.diskoverorta.utils;

import java.io.*;
import java.util.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import edu.stanford.nlp.util.StringUtils;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
/**
 * Created by naren on 22/1/15.
 */
public class WriteToCSV {

    /**
     *
     * @throws IOException
     */
    public static void writeMapAsCSV(Map<String, Set<String>> data) throws IOException {

        FileWriter fp = new FileWriter(new File("/home/naren/Desktop/output.csv"));
        BufferedWriter bw = new BufferedWriter(fp);

        for(String temp : data.keySet())
        {
            if(!data.get(temp).isEmpty() && !data.get(temp).contains(temp))
            bw.write(temp+"\t"+data.get(temp)+"\n");
        }
        bw.close();

    }


}

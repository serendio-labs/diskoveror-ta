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

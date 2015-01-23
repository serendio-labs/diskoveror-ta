package com.diskoverorta.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
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

        StringWriter sw = new StringWriter();
        CsvMapWriter writer = new CsvMapWriter(sw, CsvPreference.STANDARD_PREFERENCE);

        String[] headers = data.keySet().toArray(new String[0]);
        writer.writeHeader(headers);
//        StringUtils.join(data.values(), ","); // "val1,val2"
        writer.write(data, headers);
        writer.close();

        System.out.println("Generated CSV : \n");
        System.out.println(sw.toString());

        try {
            Files.write(sw.toString(), new File("/home/naren/Desktop/output.csv"), Charsets.UTF_8);
        } catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    public static void main(String args[])
//    {
//        try {
//            writeMapAsCSV();
//        }
//        catch (IOException e)
//        {
//            System.out.println("Cannot write to CSV");
//        }
//    }

}

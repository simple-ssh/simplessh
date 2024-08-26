package simplessh.com;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author Corneli F.
 *
 * Helpers file
 */
public class Helpers {

    private static final Logger logger = LogManager.getLogger(Helpers.class);

 public static String getJarPath(){
       String path = Helpers.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       String newPath = "";
       try {

           String[] split = path.split("/");
           for (int i = 0; i < split.length - 1; i++) {
               if (!split[i].isEmpty())
                   newPath = i == 0 ? "/" + split[i] : newPath + "/" + split[i];
           }

       }catch (Exception e){newPath = ".";}
     return newPath;
 }



    /**
     * According to the syntax of the shell command, the string is escaped by
     * enclosing it with single quote.
     *
     * eg. 11'22 ==> '11'\''22'
     *
     *
     */
    public static String escapeShellSingleQuoteString(String s, boolean addOuterQuote) {
        String replace = s.replace("'", "'\\''");
        return addOuterQuote ?  "'" + replace + "'" : replace;
    }




    public static  boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }



    /**
     * According to the syntax of the shell command, the string is escaped by
     * enclosing it with double quote.
     *
     * eg. 11\22"33$44`55 ==> "11\\22\"33\$44\`55"
     *
     *
     */
    public static String escapeShellDoubleQuoteString(String s ) {
        final List<String> targets = Arrays.asList( "\"", "$", "`");
        String escape = escape(s, "\\", targets);
          escape = escape.replace("'", "'\\''");
          //escape = escape.replaceAll("\\\"", "\\\\\"");

        return  escape;
    }

    private static String escape(String s, String escaper, List<String> targets) {
        s = s.replace(escaper, escaper + escaper);
        for (String t : targets) {
            s = s.replace(t, escaper + t);
        }
        return s;
    }


    /**
     * Get comand from json and convert them in full command
     * @param file
     * @return
     */
    public static String getFileContent(File file){
        if (!file.exists() ) { //&& !file.mkdirs()
            return "";
        }
        String data = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                data = data + line;
            }
            br.close();

        }catch (Exception e){
            System.out.println( "Error read file: " + e);
            return "";
        }
        return data;
    }


    public static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String getAlphaString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
    public static String[] createMysql(String password, String[] arr){
        String[] pass = {password};
        if(arr==null || arr.length==0)
            return pass;

        String[] result = new String[arr.length + 1];
        System.arraycopy(pass, 0, result, 0, 1);
        System.arraycopy(arr, 0, result, 1, arr.length);
        return result;
    }



    /**
     * will read InputStream and put in string
     * @param inputStream
     * @return
     */
    public static String inputStreamToString(InputStream inputStream, String type){
        StringBuilder textBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {

            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch (Exception e){
            logger.error("Error: "+e.getMessage());
        }
        //System.out.println(textBuilder.toString());
        if(type.contains("err"))
            logger.error("Error: "+textBuilder);
        return textBuilder.toString();
    }

    /**
     *  get first digit number from string or return -1 if not found
     * @param str
     * @return
     */
    public static int getFirstIntFromString(String str){
       return Arrays.stream(str.split("\\r?\\n")).
                filter(e->e.matches("\\d+")).
                mapToInt(Integer::parseInt).
                findFirst().orElse(-1);
    }
}

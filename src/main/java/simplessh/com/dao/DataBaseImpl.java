package simplessh.com.dao;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class DataBaseImpl {
    /**
     * will transform the string bellow in  List<Map<String,String>>
      Field  Type	         Null	Key	  Default	 Extra
      e1     int	         NO	    PRI	  NULL	     auto_increment
      e2	  varchar(255)  YES	          NULL
     *
     * @param data
     * @return
     */
    public List<Map<String,String>> extractTheData(String data){
        data = data.trim().replaceAll("\t", "~~@~~");

        String header[] = data.split("\\r?\\n")[0].split("~~@~~");

        return Arrays.stream(data.split("\\r?\\n")).skip(1).
                filter(st->!st.contains("file:") && !st.contains(".key") && !st.isEmpty()).
                map(st->{
                      String[] splitRows= st.split("~~@~~");
                      return IntStream.range(0, header.length)
                              .boxed()
                              .collect(Collectors.toMap(
                                    i -> header[i],  // Key: element of the array
                                    i -> (i < splitRows.length ? splitRows[i].replaceAll("\\\\\\\\", "\\\\") : "") // Value: index of the element
                            ));
                }).collect(Collectors.toList());
    }
}

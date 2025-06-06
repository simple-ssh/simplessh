package simplessh.com.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

public class TestController {

    @CrossOrigin
    @GetMapping("/get-domains")
    public List<Map<String,String>> getData(){
      //return List.of(Map.of("name","ecomm.com"),Map.of("name","simplessh.com"));
      Map<String,String> map = new HashMap<>();
      map.put("a","b");
      map.put("b","e");
      map.put("c","t");
      map.put("d","k");
      map.put("g","n");
      map.put("e","f");
      List<String> n= map.entrySet().stream().map(e->e.getKey()).collect(Collectors.toList());
        System.out.println(n);

        Map<String,String> map2 = new HashMap<>();
      map2.put("a","2");
      map2.put("b","3");
      map2.put("c","3");
      map2.put("d","7");
      map2.put("g","1");
      map2.put("e","0");

      Map<String,String> n2 = map2.entrySet().stream().collect(Collectors.toMap(e->e.getValue(),e->e.getKey(), (e1,e2)->e1+","+e2));
        System.out.println(n2);

        return null;
    }

}

package simplessh.com.response;

import lombok.Data;

@Data
public class FileData {
    private String name;
    private String type;
    private String size;
    private String date;
    private String group;
    private String owner;
    private String permission;
    private String[] str;

    public FileData(String data){
        this.str = data.split("\\s+");

        String typeFile  = getParts(0).substring(0, 1);
        this.name        = getParts(6);
        this.type        = typeFile.equals("d") || typeFile.equals("l") ? "1" : "2";
        this.owner       = getParts(2);
        this.group       = getParts(3);
        this.size        = toSize();
        this.date        = getParts(5);
        this.permission  = getParts(0);
    }

    /*drwxr-xr-x 53 root root 5kB 06/04/2024-18:23:42 lib
        System.out.println("Permissions: " + str[0]);
        System.out.println("Owner: " + str[2]);
        System.out.println("Group: " + str[3]);
        System.out.println("Size: " + str[4]);
        System.out.println("Date: " + str[5]);
        System.out.println("Time: " + str[6]);*/

    private String getParts(int n){
      return str.length >=n ? str[n] :"";
    }

    private String toSize(){
        String unit  = getParts(4);
        try{
            double fileSize = Double.parseDouble(unit.replaceAll("[^\\d]", "")); // Extract numeric part

            if (fileSize < 1024) {
                return unit;
            } else if (fileSize < 1024 * 1024) {
                return String.format("%.2f MB", fileSize / 1024);
            } else {
                return String.format("%.2f GB", fileSize / (1024 * 1024));
            }
        }catch (Exception e){
            return unit;
        }
    }
}

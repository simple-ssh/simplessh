package simplessh.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import simplessh.com.dao.DownloadFile;
import simplessh.com.response.FileData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Corneli F.
 */
@Service
public class FileManagerServices{
  

    private SaveContentInFileService saveContentService;

    private SshCommand ssh;

    public FileManagerServices(SaveContentInFileService saveContentService, SshCommand ssh) {
        this.saveContentService = saveContentService;
        this.ssh = ssh;
    }

    /**
     * get list of files folders inside directory
     * @param id
     * @param request
     * @return
     */
    public List<FileData> getFileFileList(String id, HttpServletRequest request) {
        String path = request.getParameter("directory");
        return getList(id,path);
    }

    /**
     * process list
     * @param id
     * @param path
     * @return
     */
    public List<FileData> getList(String id, String path){
        String data = ssh.execute("show_folder_content_ls_short_and_full", id, path);

        return Arrays.stream(data.split("\\r?\\n")).skip(1).map(e-> new FileData(e))
                     .sorted(Comparator.comparing(e->e.getType())).toList();
    }


    /**
     * get file content
     * @param id
     * @param request
     * @return
     */
    public String getFileContent(String id, HttpServletRequest request) {

        return ssh.getStringFileContent(request.getParameter("pathFile"), id);
     }

    /**
     * get content put in tmp file and upload to server after move back to directory where was
     * we choos this method because some file are big and this method work fast plus no need to play around with
     * escapes characters
     * @param id
     * @param list
     * @return
     */
    public String saveContent(String id, List<Map<String, String>> list) {
        String message = "ok";
        if(ssh.isFast(id)) {
           saveContentService.save(list,  id);
        }else{
           list.stream().collect(Collectors.groupingBy(e->e.get("path"))).forEach((k,v)->{
                Map<String, InputStream> file =  v.stream().collect(Collectors.toMap(e->e.get("fileName"),
                              e->new ByteArrayInputStream(e.get("content").getBytes())));

               ssh.sftpUpload(id, file, k, v.get(0).get("owner"), v.get(0).get("permission"));
            });
         }
         /*
        try {



            Map<String, List<Map<String, String>>> listMap =
                    list.stream().collect(Collectors.groupingBy(e->e.get("path")));
            listMap.forEach((k,v)->{

                //saveContentService.save(e.get("content"));

                Map<String, InputStream> file =  v.stream().collect(Collectors.toMap(e->e.get("fileName"),
                        e->new ByteArrayInputStream(e.get("content").getBytes())));
               if(isFast(id)) {
                   sftpFastUpload(id, file, k, v.get(0).get("owner"), v.get(0).get("permission"));
               }else{
                   sftpUpload(id, file, k, v.get(0).get("owner"), v.get(0).get("permission"));
               }
            });

        } catch (Exception e) {
            System.out.println("An error during the save file content:"+e.getMessage());
            message =e.getMessage();
        }*/

        return message;
    }

    /**
     * rename file folder
     * @param data
     * @return
     */
    public String renameFile(String id, Map<String, String> data) {
        String oldname= data.getOrDefault("fromName","");
        return ssh.execute("rename", id,
                              oldname.replaceAll("\\(","\\\\(").
                                      replaceAll("\\)","\\\\)"),
                              data.getOrDefault("toName",""));
    }

    /**
     * will create new file or folder
     * @param id
     * @param data
     * @return
     */
    public List<FileData> newFileFolder(String id, Map<String, String> data) {
        String type = data.getOrDefault("typeNew","");

        ssh.execute(type.contains("file") ? "new_empty_file" : "new_directory", id,
                     data.getOrDefault("owner",""),
                     data.getOrDefault("name",""));

        return  getList(id,data.getOrDefault("currentPath",""));
    }

    /**
     * remove file or follder
     * @param id
     * @param data
     * @return
     */
    public List<FileData> removeFileFolder(String id, Map<String, String> data) {

        String checkDir= ssh.execute("check_if_directory_exist", id, "/var/trash/");

        if(!checkDir.contains("yes"))
            ssh.execute("new_directory", id, "www-data", "/var/trash/");

        String fileList = data.getOrDefault("fileList","");

        ssh.execute("move", id,  fileList.replaceAll("\\(","\\\\(").
                                                           replaceAll("\\)","\\\\)"),
                             "/var/trash/");
        return  getList(id,data.getOrDefault("currentPath",""));
    }

    /**
     * paste cut file or directory
     * @param id
     * @param data
     * @return
     */
    public List<FileData> pasteFileFolder(String id, Map<String, String> data) {
        String typePaste=data.getOrDefault("typePaste","");

        String fileList = data.getOrDefault("fileList","");

        ssh.execute(typePaste.contains("move") ? "move" : "copy", id,
                               fileList.replaceAll("\\(","\\\\(").
                                        replaceAll("\\)","\\\\)"),
                               data.getOrDefault("currentPath",""));

        return  getList(id,data.getOrDefault("currentPath",""));
    }

    /**
     * empty file content
     * @param id
     * @param data
     * @return
     */
    public List<FileData> emptyFile(String id, Map<String, String> data) {

        ssh.execute("empty_file_content", id, data.getOrDefault("filePath",""));

        return  getList(id,data.getOrDefault("currentPath",""));
    }

    /**
     * change owner of file or directory
     * @param id
     * @param data
     * @return
     */
    public List<FileData> changeOwnerPermission(String id, Map<String, String> data) {
        String type             = data.getOrDefault("type","");
        String yesSubPermission = data.getOrDefault("yesSubPermission","");
        String subPermission    = data.getOrDefault("subPermission","");
        String paths            = data.getOrDefault("filePath","");
        String owner            = data.getOrDefault("owner","");


        if(type.contains("owner")){
            ssh.execute((owner.contains(":") ? "modify_owner_user_group":"modify_owner_group"), id, owner, paths);
        }else if(type.contains("folder")){
            ssh.execute("file_permission", id, data.getOrDefault("permissions",""), paths);

            if(!yesSubPermission.isEmpty() && !subPermission.isEmpty()){
                ssh.execute("all_folders_permission", id, paths, subPermission);

                ssh.execute("all_files_permission", id, paths, subPermission);
            }

        }else if(type.contains("file")){

            ssh.execute(paths.contains(" ")? "file_permission_all" : "file_permission", id,
                          data.getOrDefault("permissions",""), paths);

        }
        return  getList(id, data.getOrDefault("currentPath",""));
    }

    /**
     * empty trash
     * @param id
     * @param request
     * @return
     */
   public List<FileData> emptyTrash(String id, HttpServletRequest request) {
        String currentUrl = request.getParameter("currentPath");
        ssh.execute("empty_trash", id);
        return  getList(id, currentUrl);
    }

    /**
     * put in zip or unzip file or directories
     * @param id
     * @param data
     * @return
     */
    public List<FileData> addToZipUnzip(String id, Map<String, String> data) {
        String type= data.getOrDefault("type","");
        String filePath= data.getOrDefault("filePath","");
        String currentPath= data.getOrDefault("currentPath","");

        if(type.contains("unzip")){
            String end = Stream.of(filePath.split("\\.")).reduce((first, last)->last).get();
            ssh.execute((end.contains("gz") ||end.contains("tar") ? "tarunzip":"unzip"), id, filePath, currentPath);
        }else{
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
            Date date = new Date(System.currentTimeMillis());
            ssh.execute("zip", id, (currentPath +"/"+ formatter.format(date) + ".zip"), filePath);
        }

        return  getList(id,currentPath);
    }

    /*
    @GetMapping(value="/download-file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] downloadFile(HttpServletRequest request)  throws IOException {
        connect();
        String path = request.getParameter("pathFile");
        InputStream stream= downloadFileStream(path);

        return stream !=null? org.apache.commons.io.IOUtils.toByteArray(stream) :null;
    }*/

    /**
     * upload file to server
     * @param id
     * @param request
     * @param files
     * @return
     */
    public List<FileData> uploadToServer(String id, HttpServletRequest request,
                                                   MultipartFile[] files) {
        String currentPath= request.getParameter("currentPath");

        Map<String, InputStream> listF = new HashMap<>();
        for(MultipartFile file : files){
            try {
                listF.put(file.getOriginalFilename(), file.getInputStream());
            }catch (Exception e){}
        }

        String owner=  ssh.execute("get_folder_group", id, currentPath);

        if(ssh.isFast(id)) {
            ssh.sftpFastUpload(id,listF, currentPath, owner.trim(),"644");
        }else{
            ssh.sftpUpload(id,listF, currentPath, owner.trim(),"644");
        }

        return  getList(id,currentPath);
    }

    /**
     * download file
     * @param request
     * @param response
     * @throws IOException
     */
    public void downloadPDFResource( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        String pathToFile= request.getParameter("pathToFile");
        String fileName= request.getParameter("fileName");

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));

        String connectionID = request.getParameter("id");
        DownloadFile downloadFile =ssh.downloadFileStream(pathToFile, connectionID);
        try {
            InputStream inp= downloadFile.getFile();
            FileCopyUtils.copy(inp, response.getOutputStream());
        }catch (Exception e){

        }


        ssh.disconnectSFTP(downloadFile.getChannelDownload(),
                downloadFile.getChannelSftpDownload() );

/*
        Channel channel =  null;
        ChannelSftp channelSftpInt = null;
        connect();
        try{
            channel=getSession().openChannel("sftp");
            channel.connect();
            channelSftpInt = (ChannelSftp) channel;
            InputStream inp= channelSftpInt.get(pathToFile);
            FileCopyUtils.copy(inp, response.getOutputStream());
        }catch(Exception e){
            System.out.println("Error Run Download:"+e);
        }finally {
           if(channel != null)
                channel.disconnect();
           if(channelSftpInt != null)
                channelSftpInt.disconnect();
           disconnect();
        }*/
    }

    /**
     * get size of all files folders inside a directory, very utils staff
     * @param id
     * @param request
     * @return
     */
     public String getFolderSize(String id, HttpServletRequest request) {
        return ssh.execute("get_all_fil_folder_size", id, request.getParameter("directory"));
    }
}

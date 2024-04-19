package simplessh.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simplessh.com.response.FileData;
import simplessh.com.services.FileManagerServices;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author Corneli F.
 * File manager controller
 */

@RestController
@RequestMapping("/api/v1/")
public class FileManagerController {

    @Autowired
    private FileManagerServices service;

    /**
     * get list of files folders inside directory
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-list-of-files")
    public List<FileData> getFileFileList(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.getFileFileList(id,request);
    }

    /**
     * get file content
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-file-content")
    public String getFileContent(@RequestHeader("id") String id, HttpServletRequest request) {

        return service.getFileContent(id, request);
    }

    /**
     * get content put in tmp file and upload to server after move back to directory where was
     * we choos this method because some file are big and this method work fast plus no need to play around with
     * escapes characters
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/save-file-content" , consumes = "application/json", produces = "application/json")
    public String saveContent(@RequestHeader("id") String id, @RequestBody List<Map<String, String>> data) {

        return service.saveContent(id, data);
    }

    /**
     * rename file folder
     * @param data
     * @return
     */
    @PutMapping(path = "/rename-file" , consumes = "application/json", produces = "application/json")
    public String renameFile(@RequestHeader("id") String id, @RequestBody Map<String, String> data) {

        return service.renameFile(id, data);
     }

    /**
     * will create new file or folder
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/new-file-folder" , consumes = "application/json", produces = "application/json")
    public List<FileData> newFileFolder(@RequestHeader("id") String id,
                                                  @RequestBody Map<String, String> data) {


        return  service.newFileFolder(id, data);
    }

    /**
     * remove file or follder
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/remove-file-folder" , consumes = "application/json", produces = "application/json")
    public List<FileData> removeFileFolder(@RequestHeader("id") String id,
                                                     @RequestBody Map<String, String> data) {

        return service.removeFileFolder(id, data);
    }

    /**
     * paste cut file or directory
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/paste-file-folder" , consumes = "application/json", produces = "application/json")
    public List<FileData> pasteFileFolder(@RequestHeader("id") String id,
                                                    @RequestBody Map<String, String> data) {

        return service.pasteFileFolder(id, data);
    }

    /**
     * empty file content
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/empty-file-content", consumes = "application/json", produces = "application/json")
    public List<FileData> emptyFile(@RequestHeader("id") String id,
                                              @RequestBody Map<String, String> data) {

        return  service.emptyFile(id, data);
    }

    /**
     * change owner of file or directory
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/change-owner-permission", consumes = "application/json", produces = "application/json")
    public List<FileData> changeOwnerPermission(@RequestHeader("id") String id,
                                                          @RequestBody Map<String, String> data) {

        return service.changeOwnerPermission(id, data);
    }

    /**
     * empty trash
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/empty-trash")
    public List<FileData> emptyTrash(@RequestHeader("id") String id, HttpServletRequest request) {

      return service.emptyTrash(id,request);
    }

    /**
     * put in zip or unzip file or directories
     * @param id
     * @param data
     * @return
     */
    @PutMapping(path = "/add-to-archive-unzip", consumes = "application/json", produces = "application/json")
    public List<FileData> addToZipUnzip(@RequestHeader("id") String id,
                                                  @RequestBody Map<String, String> data) {

      return service.addToZipUnzip(id, data);
   }
    /*
    @GetMapping(value="/download-file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] downloadFile(HttpServletRequest request)  throws IOException {
        ssh.connect();
        String path = request.getParameter("pathFile");
        InputStream stream= ssh.downloadFileStream(path);

        return stream !=null? org.apache.commons.io.IOUtils.toByteArray(stream) :null;
    }*/

    /**
     * upload file to server
     * @param id
     * @param request
     * @param files
     * @return
     */
    @PutMapping(path = "/upload-to-server")
    public List<FileData> uploadToServer(@RequestHeader("id") String id, HttpServletRequest request,
                                         @RequestParam("files") MultipartFile[] files) {


       return service.uploadToServer(id, request, files);
    }

    /**
     * download file
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download-file")
    public void downloadPDFResource( HttpServletRequest request,
                                     HttpServletResponse response ) throws IOException {

        service.downloadPDFResource(request,response);
    }

    /**
     * get size of all files folders inside a directory, very utils staff
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get-size-folder")
    public String getFolderSize(@RequestHeader("id") String id, HttpServletRequest request) {

        return  service.getFolderSize(id, request);
    }
 }

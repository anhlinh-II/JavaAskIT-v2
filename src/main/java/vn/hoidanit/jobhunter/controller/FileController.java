package vn.hoidanit.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

     @Value("${hoidanit.upload-file.base-uri}")
     private String baseURI;

     private final FileService fileService;

     @PostMapping("/files")
     public ResponseEntity<ResUploadFileDTO> upload(
               @RequestParam(name = "file", required = false) MultipartFile file,
               @RequestParam("folder") String folder
     ) throws URISyntaxException, IOException, StorageException {

          // skip validate
          if (file == null || file.isEmpty()) {
               throw new StorageException("file is empty, please upload a file");
          }
          String fileName = file.getOriginalFilename();
          List<String> allowedExtention = Arrays.asList("doc", "jpg", "png", "pdf", "docx", "jpeg");
          boolean isValid = allowedExtention.stream().anyMatch(i -> i.toLowerCase().endsWith(i)); 

          if (isValid) {
               throw new StorageException("invalid file exception. only allows " + allowedExtention.toString());
          }
          // create a directory if not exist
          this.fileService.createDirectory(baseURI + folder);
          // store file
          String uploadFile = this.fileService.store(file, folder);

          ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());
          return ResponseEntity.ok().body(res);
     }
}

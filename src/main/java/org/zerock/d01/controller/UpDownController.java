package org.zerock.d01.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.d01.dto.upload.UploadFileDTO;
import org.zerock.d01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UpDownController {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Operation(summary = "Upload POST", description = "POST 방식으로 파일등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(@Parameter(description = "Files to be upload", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))UploadFileDTO uploadFileDTO){
        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null){

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach( multipartFile -> {
                String originName = multipartFile.getOriginalFilename();
                log.info(originName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_"+originName);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);

                    //이미지 파일이면 썸네일저장
                    if(Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        File thumFile = new File(uploadPath, "s_"+uuid+"_"+originName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumFile, 200, 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originName)
                                .img(image)
                                .build());
            });//end for
            return list;
        }//end if
        return null;
    }
    @Operation(summary = "View 파일", description = "GET 방식으로 첨부파일 조회")
    @GetMapping("view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        log.info("resource: " + resource);
        String resourceName = resource.getFilename();
        log.info("resourceName: " + resourceName);
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            log.info("content-type" +Files.probeContentType(resource.getFile().toPath()));
           return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(summary = "remove 파일", description = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable("fileName")String fileName){
        Resource resource = new FileSystemResource(uploadPath + File.separator+fileName);

        String resourceFilename = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){
                                            //C:\\upload \
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultMap.put("result" , removed);
        return resultMap;
    }
}

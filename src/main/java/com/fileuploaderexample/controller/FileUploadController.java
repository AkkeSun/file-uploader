package com.fileuploaderexample.controller;

import com.fileuploaderexample.service.FileUploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload-image")
    public String fileUpload(@RequestPart MultipartFile file, String serviceName) throws IOException {
        return fileUploadService.fileUpload(file, serviceName);
    }

}

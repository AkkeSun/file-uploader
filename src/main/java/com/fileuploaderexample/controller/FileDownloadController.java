package com.fileuploaderexample.controller;

import com.fileuploaderexample.service.FileDownloadService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadByImagePath(String filePath) { // 절대경로를 포함한 이미지명
        return fileDownloadService.imageDownload(filePath);
    }
}

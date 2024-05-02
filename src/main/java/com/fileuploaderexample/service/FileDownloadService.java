package com.fileuploaderexample.service;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    public ResponseEntity<Resource> imageDownload(String filePath) {
        try {
            File file = new File(filePath);
            FileSystemResource resource = new FileSystemResource(file);
            Tika tika = new Tika();
            String mediaType = tika.detect(file);

            String[] pathComponents = filePath.split("/");
            String fileName = pathComponents[pathComponents.length - 1];

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, mediaType)
                .header(HttpHeaders.CONTENT_LENGTH, file.length() + "")
                .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

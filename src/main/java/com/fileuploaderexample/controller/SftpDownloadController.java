package com.fileuploaderexample.controller;

import com.fileuploaderexample.service.SftpDownloadService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SftpDownloadController {

    private final SftpDownloadService sftpDownloadService;
    @GetMapping("/sftp/download")
    public ResponseEntity<Resource> sftpFileDownload(String filePath) throws JSchException, SftpException, IOException {
        return sftpDownloadService.downloadImage(filePath);
    }
}

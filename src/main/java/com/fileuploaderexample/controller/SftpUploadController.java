package com.fileuploaderexample.controller;

import com.fileuploaderexample.service.SftpUploadService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SftpUploadController {

    private final SftpUploadService sftpService;

    @PostMapping("/sftp/upload")
    public String upload(@RequestPart MultipartFile file)
        throws JSchException, SftpException, IOException {
        return sftpService.uploadImage(file);
    }

}

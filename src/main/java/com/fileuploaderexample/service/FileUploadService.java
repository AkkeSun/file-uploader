package com.fileuploaderexample.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileUploadService {

    @Value("${upload.domain}")
    private String uploadDomain;

    @Value("${spring.servlet.multipart.location}")
    private String defaultLocation;

    public String fileUpload(MultipartFile file, String serviceName) throws IOException {

        File folder = new File(defaultLocation + serviceName + "/" + getTodayDatePath());
        if(!folder.exists()) {
            folder.mkdirs(); // 디렉토리가 없는 경우 디렉토리 생성
        }

        String fileName = file.getOriginalFilename().replace(" ", "_");
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileBaseName = fileName.substring(0, fileName.lastIndexOf("."));
        String randomString = generateRandomString();

        String uploadFileName = serviceName + "/" + getTodayDatePath()
            + fileBaseName + randomString + "." + fileExtension;

        File fileForUpload = new File(uploadFileName); // 업로드할 파일 생성
        file.transferTo(fileForUpload); // 파일 업로드

        return uploadDomain + uploadFileName;
    }

    private String generateRandomString() {
        int leftLimit = 'A';
        int rightLimit = 'Z';
        int targetStringLength = 7;
        Random random = new Random();

        return "_" + random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    private String getTodayDatePath() {
        LocalDate today = LocalDate.now();
        return today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth() + "/";
    }
}

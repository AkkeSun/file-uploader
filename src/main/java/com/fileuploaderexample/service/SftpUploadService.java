package com.fileuploaderexample.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
    이미지 서버에 API 를 배포할 수 없는 경우
 */
@Log4j2
@Service
public class SftpUploadService {

    @Value("${image.server.host}")
    private String host;

    @Value("${image.server.port}")
    private int port;

    @Value("${image.server.username}")
    private String user;

    @Value("${image.server.password}")
    private String password;

    @Value("${image.server.path}")
    private String path;


    public String uploadImage(MultipartFile image) throws JSchException, SftpException, IOException {
        ChannelSftp channelSftp = channelConnect();
        String directory = checkAndMakeDirectories(channelSftp);
        String uploadFileName = getUploadFileName(image);

        InputStream inputStream = new BufferedInputStream(image.getInputStream());

        // upload
        channelSftp.put(inputStream, directory + "/" + uploadFileName);
        channelSftp.quit();
        channelSftp.disconnect();
        return "http://" + host + directory + "/" + uploadFileName;
    }

    private ChannelSftp channelConnect() throws JSchException {

        JSch jSch = new JSch();
        Session session = jSch.getSession(user, host, port);
        session.setPassword(password);

        Properties config = new Properties();
        // 호스트 정보를 검사하지 않는다.
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        // 접속
        session.connect(3000);

        // sftp 연결
        Channel channel = session.openChannel("sftp");
        channel.connect();

        // 채널을 ssh용 채널 객채로 캐스팅
        return (ChannelSftp) channel;
    }

    private String checkAndMakeDirectories(ChannelSftp channelSftp) throws SftpException {
        LocalDate now = LocalDate.now();
        StringBuffer sb = new StringBuffer();

        sb.append(path);
        sb.append(now.getYear());
        checkAndMakeDirectory(channelSftp, sb.toString());

        sb.append("/");
        sb.append(now.getMonthValue());
        checkAndMakeDirectory(channelSftp, sb.toString());

        sb.append("/");
        sb.append(now.getDayOfMonth());
        checkAndMakeDirectory(channelSftp, sb.toString());

        return sb.toString();
    }

    private void checkAndMakeDirectory(ChannelSftp sftp, String directory) throws SftpException {
        try {
            sftp.stat(directory);
        } catch (Exception e) {
            log.info("Create directory! : " + directory);
            sftp.mkdir(directory);
        }
    }

    private String getUploadFileName(MultipartFile image) {
        String fileName = image.getOriginalFilename().replace(" ", "_");
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileBaseName = fileName.substring(0, fileName.lastIndexOf("."));
        return  fileBaseName + generateRandomString() + "." + fileExtension;
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
}

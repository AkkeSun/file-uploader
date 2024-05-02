package com.fileuploaderexample.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/*
    이미지 서버에 API 를 배포할 수 없는 경우
 */
@Service
public class SftpDownloadService {

    @Value("${image.server.host}")
    private String host;

    @Value("${image.server.port}")
    private int port;

    @Value("${image.server.username}")
    private String user;

    @Value("${image.server.password}")
    private String password;

    public ResponseEntity<Resource> downloadImage(String filePath) throws JSchException, SftpException, IOException {
        ChannelSftp channelSftp = channelConnect();
        BufferedInputStream bis = new BufferedInputStream(channelSftp.get(filePath));

        String[] pathComponents = filePath.split("/");
        String fileName = pathComponents[pathComponents.length - 1];

        File file = new File(fileName);
        FileSystemResource resource = new FileSystemResource(file);

        try (OutputStream os = resource.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        Tika tika = new Tika();
        String mediaType = tika.detect(file);

        bis.close();
        channelSftp.quit();
        channelSftp.disconnect();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + fileName )
            .header(HttpHeaders.CONTENT_TYPE, mediaType)
            .header(HttpHeaders.CONTENT_LENGTH, file.length() + "")
            .body(resource);
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
}


# File Upload & Download Example

---

## File Upload API

1. HTTP Method : POST

2. URI : http://localhost:8080/upload

3. Request
   - file : 업로드할 파일
   - serviceName : 서비스 이름 (폴더명)

4. Response
   - 업로드한 이미지 호출 도메인

----

## File Download API

1. HTTP Method : GET

2. URI : http://localhost:8080/download

3. Request
   - filePath : 파일 경로

4. Response
   - 파일 리소스

----

## SFTP File Upload API
이미지 서버에 API 를 배포하지 못하는 경우 사용합니다.

1. HTTP Method : POST

2. URI : http://localhost:8080/sftp/upload

3. Request
   - file : 업로드할 파일

4. Response
   - 업로드한 이미지 호출 도메인

----

## SFTP File Download API
이미지 서버에 API 를 배포하지 못하는 경우 사용합니다.

1. HTTP Method : GET

2. URI : http://localhost:8080/sftp/download

3. Request
   - filePath : 파일 경로

4. Response
   - 파일 리소스
----
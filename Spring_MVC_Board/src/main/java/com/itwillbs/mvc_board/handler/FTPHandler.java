package com.itwillbs.mvc_board.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.ibatis.io.Resources;

public class FTPHandler {
	// FTP 연결 정보를 관리하는 org.apache.commons.net.ftp.FTPClient 타입 멤버변수 선언
	private FTPClient ftpClient;
	
	// 생성자 정의 - FTPClient 인스턴스 생성
	public FTPHandler() {
		ftpClient = new FTPClient();
	}
	
	// FTP 연결 및 설정을 담당할 connect() 메서드 정의
	// => 파라미터 : FTP 기본 디렉토리(String baseDir)
	public void connect(String baseDir) {
		try {
			// FTP 연결 및 로그인에 사용할 프로퍼티 파일 읽어오기
			// => src/main/resources/config/ftp.properties
			String propsFileName = "config/ftp.properties"; // 경로 지정
			Properties properties = new Properties(); // Properties 객체 생성
			// org.apache.ibatis.io.Resources 객체의 getResourceAsReader() 메서드를 호출하여
			// 지정한 프로퍼티 파일 읽어오기(java.io.Reader 타입 객체 리턴)
			Reader reader = Resources.getResourceAsReader(propsFileName);
			// Properties 객체의 load() 메서드를 호출하여 Reader 객체를 파라미터로 전달
			properties.load(reader);
			
			// 읽어온 프로퍼티 파일에 저장된 프로퍼티(host, port, id, passwd)를 가져와서 변수에 저장
			String host = properties.getProperty("ftp.host");
			int port = Integer.parseInt(properties.getProperty("ftp.port"));
			String id = properties.getProperty("ftp.id");
			String passwd = properties.getProperty("ftp.passwd");
			
			// FTP 연결
			ftpClient.setControlEncoding("UTF-8"); // FTP 인코딩 방식 설정(연결 전 설정 필수!)
			// FTPClient 객체의 connect() 메서드를 호출하여 FTP 연결 시도
			// => 파라미터 : 호스트 주소, 포트 번호   리턴타입 : void
			ftpClient.connect(host, port);
			// 연결 성공 시 리턴되는 값이 없으므로 FTPClient 객체의 getReplyCode() 메서드를 호출하여
			// 응답되는 코드값을 전달
			int connectResult = ftpClient.getReplyCode();
			// FTPReply.isPositiveCompletion() 메서드를 호출하여 리턴받은 코드값 판별
			if(!FTPReply.isPositiveCompletion(connectResult)) { // 부정적인 코드일 경우(= 연결 실패)
				System.out.println("FTP 서버 연결 실패!");
				throw new Exception("FTP 서버 연결 실패!");
			} 
			
			// FTP 서버 로그인
			boolean isLoginSuccess = ftpClient.login(id, passwd);
			// 로그인 실패 여부 판별
			if(!isLoginSuccess) {
				disconnect(); // 로그인 실패 시 서버 연결 끊기
				System.out.println("FTP 서버 로그인 실패!");
				throw new Exception("FTP 서버 로그인 실패!");
			} 
			
			// FTP 서버 접속 후 부가 설정
			ftpClient.setSoTimeout(1000 * 60); // 기본 타임아웃 60초로 설정(1000ms = 1s * 10 = 10s)
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 파일 타입 설정
			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE); // 파일 타입 설정
			ftpClient.enterLocalPassiveMode(); // FTP 접속 모드를 Passive 모드로 설정
			boolean existsDir = ftpClient.changeWorkingDirectory(baseDir); // 기본 디렉토리 변경
			// 만약, 기본 디렉토리가 존재하지 않을 경우 해당 디렉토리 생성 후 다시 변경
			if(!existsDir) {
				ftpClient.makeDirectory(baseDir); // 경로 생성
				ftpClient.changeWorkingDirectory(baseDir); // 경로 변경
			}
			
			System.out.println("FTP 서버 접속 성공! - " + ftpClient.printWorkingDirectory());
//			System.out.println(ftpClient.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	// FTP 로그아웃 및 연결 해제 작업을 수행하는 disconnect() 메서드 정의
	public void disconnect() {
		try {
			// FTPClient 객체의 isConnected() 메서드를 통해 접속 여부 판별하여 접속 중일 경우 해제
			if(ftpClient.isConnected()) {
				ftpClient.logout(); // 로그아웃
				ftpClient.disconnect(); // 연결 해제
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// FTP 서버 업로드
	public void upload(File f, String real_file) {
		// 해당 File 객체를 입력스트림에 연결하기 위해 FileInputStream 객체 생성
		// => try ~ resources 구문 활용
		try(FileInputStream fis = new FileInputStream(f)) {
			// FTPClient 객체의 storeFile() 메서드를 호출하여 파일 업로드 작업 수행
			// => 파라미터 : 업로드 할 파일명, 실제 파일이 연결된 입력스트림
			boolean isUploadSuccess = ftpClient.storeFile(real_file, fis);
			System.out.println("업로드 결과 : " + isUploadSuccess);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// FTP 서버 다운로드
	public void download(File f, String fileName) {
		// 해당 File 객체를 출력스트림에 연결하기 위해 FileOutputStream 객체 생성
		// => try ~ resources 구문 활용
		try (FileOutputStream fos = new FileOutputStream(f)) {
			// FTPClient 객체의 retrieveFile() 메서드를 호출하여 원본파일명, File 객체 전달
			boolean isDownloadSuccess = ftpClient.retrieveFile(fileName, fos);
			System.out.println("다운로드 결과 : " + isDownloadSuccess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}













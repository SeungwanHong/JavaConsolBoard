package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.dao.UserDao;
import server.thread.ServerThread;

public class severMain {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		
		
		Connection conn = null;
		PreparedStatement pstm = null;
		
		//라이브러리 로드는 전역적으로 해야하는 부분이기 때문에
		//DAO에 따로 배치하지 않는다.
		Class.forName("oracle.jdbc.driver.OracleDriver");
		UserDao doa = new UserDao();
		
		ServerSocket severSocket = new ServerSocket(8080);
		
		//클라이언트의 접속 받기
		//모든 서버들은 클라이언트 접속에 대해 쓰래드 처리한다.
		
		//무한루프를 이용하여 모든 클라이언트의 접속을 받아낸다.
		while(true){
			try{
				//클라이언트의 접속을 accept() 메소드로 응답 대기를 한다.
				//클라이언트가 접속하게되면 접속한 클라이언트 소켓이 리턴된다.
				Socket clientSocket = severSocket.accept();
				
				//리턴된 클라이언트 소켓을 쓰래드로 관리하여 서버는 접속한 클라이언트마다
				//쓰레드 내부에서 통신한다.
				ServerThread severThread = new ServerThread(clientSocket);
				severThread.start();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		
	}
}

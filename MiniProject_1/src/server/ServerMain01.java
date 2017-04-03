package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import server.dao.UserDao;
import server.thread.ServerThread;

public class ServerMain01 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		// 전역적으로(프로그램 시작 하자마자) 라이브러리를 로딩 해야 하기 때문에
		// DAO 에 따로 배치 하지는 않는다.
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		ServerSocket serverSocket = new ServerSocket(8080);
		
		//클라이언트의 접속 받기
		// 모든 서버들은 클라이언트의 접속에 대해 Thread를 사용한다.
		
		// 무한루프를 이용하여 모든 클라이언트의 접속을 받아낸다.
		while(true){
			try
			{
				//클라이언트의 접속을 accpet() 메소드를 이용하여 기다리다가
				// 클라이언트가 접속 하게 되면 접속한 클라이언트의 소켓이 리턴된다.
				System.out.println("접속 대기중");
				Socket clientSocket = serverSocket.accept();
				
				//리턴된 클라이언트 소켓을 쓰레드로 관리하여 서버는 접속한 클라이언트 마다
				// 쓰레드 내부에서 통신한다.
				ServerThread serverThread = new ServerThread(clientSocket);
				serverThread.start();
				
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				//serverSocket.close();
			}
		}
		
		
	}

}


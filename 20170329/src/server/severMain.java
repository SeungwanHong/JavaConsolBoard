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
		
		//���̺귯�� �ε�� ���������� �ؾ��ϴ� �κ��̱� ������
		//DAO�� ���� ��ġ���� �ʴ´�.
		Class.forName("oracle.jdbc.driver.OracleDriver");
		UserDao doa = new UserDao();
		
		ServerSocket severSocket = new ServerSocket(8080);
		
		//Ŭ���̾�Ʈ�� ���� �ޱ�
		//��� �������� Ŭ���̾�Ʈ ���ӿ� ���� ������ ó���Ѵ�.
		
		//���ѷ����� �̿��Ͽ� ��� Ŭ���̾�Ʈ�� ������ �޾Ƴ���.
		while(true){
			try{
				//Ŭ���̾�Ʈ�� ������ accept() �޼ҵ�� ���� ��⸦ �Ѵ�.
				//Ŭ���̾�Ʈ�� �����ϰԵǸ� ������ Ŭ���̾�Ʈ ������ ���ϵȴ�.
				Socket clientSocket = severSocket.accept();
				
				//���ϵ� Ŭ���̾�Ʈ ������ ������� �����Ͽ� ������ ������ Ŭ���̾�Ʈ����
				//������ ���ο��� ����Ѵ�.
				ServerThread severThread = new ServerThread(clientSocket);
				severThread.start();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		
	}
}
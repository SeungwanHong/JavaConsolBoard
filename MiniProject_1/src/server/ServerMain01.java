package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import server.dao.UserDao;
import server.thread.ServerThread;

public class ServerMain01 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		// ����������(���α׷� ���� ���ڸ���) ���̺귯���� �ε� �ؾ� �ϱ� ������
		// DAO �� ���� ��ġ ������ �ʴ´�.
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		ServerSocket serverSocket = new ServerSocket(8080);
		
		//Ŭ���̾�Ʈ�� ���� �ޱ�
		// ��� �������� Ŭ���̾�Ʈ�� ���ӿ� ���� Thread�� ����Ѵ�.
		
		// ���ѷ����� �̿��Ͽ� ��� Ŭ���̾�Ʈ�� ������ �޾Ƴ���.
		while(true){
			try
			{
				//Ŭ���̾�Ʈ�� ������ accpet() �޼ҵ带 �̿��Ͽ� ��ٸ��ٰ�
				// Ŭ���̾�Ʈ�� ���� �ϰ� �Ǹ� ������ Ŭ���̾�Ʈ�� ������ ���ϵȴ�.
				System.out.println("���� �����");
				Socket clientSocket = serverSocket.accept();
				
				//���ϵ� Ŭ���̾�Ʈ ������ ������� �����Ͽ� ������ ������ Ŭ���̾�Ʈ ����
				// ������ ���ο��� ����Ѵ�.
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


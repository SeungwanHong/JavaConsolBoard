package server.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.attribute.DosFileAttributes;
import java.sql.SQLException;

import org.omg.CORBA.Request;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;
import server.dao.UserDao;

public class ServerThread extends Thread{
	private Socket clientSocket;
	private UserDao dao;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	
	//������ ������ ������ Ŭ���̾�Ʈ�κ��� �����͸� �ްų� ������ Ŭ���̾�Ʈ���� �����͸� �����ϱ�
	//���� Ŭ���̾�Ʈ ������ �������� �ŰԺ����� �޴´�.
	public ServerThread(Socket clientSocket){
		this.clientSocket = clientSocket;
		//Thread ���� dao�� �̿��ؼ� ���� ó�����ش�.
		dao = new UserDao();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			dis = new DataInputStream(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream());
			
			int req_packet = 0;
			//Ŭ���̾�Ʈ�� ���� ��Ŷ�� ������ ������ �ݺ� ��� ��Ŷ�� ��Ŷ�� ����
			//��û ó���� �ϰڴ�.
			while(req_packet != RequestPacket.REQ_EIXT){
				
				//1) ������ Ŭ���̾�Ʈ�� ������ ��Ŷ�� �޴´�.
				req_packet = dis.readInt();
				//2)������ Ŭ���̾�Ʈ�� ���� ��Ŷ�� �м�
				switch(req_packet){
				case RequestPacket.REQ_LOGIN :
					//3)������ Ŭ���̾�Ʈ�� ���� �ǵ����͸� ó���Ѵ�.
					String id = dis.readUTF();
					String pw = dis.readUTF();
					boolean isLogin = dao.login(id, pw);
					if(isLogin){
						//�α��� ������
						//���̵������� �����ҰŴ�
						
						dos.writeInt(ResponsePacket.RSP_LOGIN_Y);
					}else{
						dos.writeInt(ResponsePacket.RSP_LOGIN_N);
					}
					break;
				case RequestPacket.REQ_JOIN : 
					//1)����ڰ� �Է��� ������ �޾� ����
					String joinid = dis.readUTF();
					String joinpw = dis.readUTF();
					String joinname = dis.readUTF();
					String joinemail = dis.readUTF();
					int result = dao.joinUser(joinid, joinpw, joinpw, joinname, joinemail);
					switch(result){
					case UserDao.JOIN_Y:
						dos.writeInt(ResponsePacket.RSP_JOIN_Y);
						break;
					case UserDao.JOIN_N:
						dos.writeInt(ResponsePacket.RSP_JOIN_N);
						break;
					case UserDao.CHECK_PW_N:
						dos.writeInt(ResponsePacket.RSP_JOIN_CHPW_N);
						break;
					case UserDao.CHECK_ID_N:
						dos.writeInt(ResponsePacket.RSP_JOIN_CHID_N);
						break;
					}
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				dis.close();
				dos.close();
				clientSocket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	
}
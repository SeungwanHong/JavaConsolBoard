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
	
	
	//쓰래드 생성시 접속한 클라이언트로부터 데이터를 받거나 접속한 클라이언트에게 데이터를 전송하기
	//위해 클라이언트 소켓을 생성자의 매게변수로 받는다.
	public ServerThread(Socket clientSocket){
		this.clientSocket = clientSocket;
		//Thread 마다 dao를 이용해서 쿼리 처리해준다.
		dao = new UserDao();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			dis = new DataInputStream(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream());
			
			int req_packet = 0;
			//클라이언트가 종료 패킷을 보내지 않으면 반복 계속 패킷과 패킷에 대한
			//요청 처리를 하겠다.
			while(req_packet != RequestPacket.REQ_EIXT){
				
				//1) 서버는 클라이언트가 보내는 패킷을 받는다.
				req_packet = dis.readInt();
				//2)서버는 클라이언트가 보낸 패킷을 분석
				switch(req_packet){
				case RequestPacket.REQ_LOGIN :
					//3)서버는 클라이언트가 보낸 실데이터를 처리한다.
					String id = dis.readUTF();
					String pw = dis.readUTF();
					boolean isLogin = dao.login(id, pw);
					if(isLogin){
						//로그인 됬을때
						//아이디정보를 저장할거니
						
						dos.writeInt(ResponsePacket.RSP_LOGIN_Y);
					}else{
						dos.writeInt(ResponsePacket.RSP_LOGIN_N);
					}
					break;
				case RequestPacket.REQ_JOIN : 
					//1)사용자가 입력한 정보를 받아 오기
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

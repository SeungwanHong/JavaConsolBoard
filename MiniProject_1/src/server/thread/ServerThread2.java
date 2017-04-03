package server.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;
import server.dao.BoardDao;
import server.dao.UserDao;

public class ServerThread2 extends Thread {

	// 접속한 클라이언트의 소켓
	private Socket clientSocket;
	private UserDao userDao = new UserDao();
	private BoardDao boardDao = new BoardDao();
	private DataInputStream dis;
	private DataOutputStream dos;
	private String session_id = "";

	private int idx = 0;
	//private boolean idxCheck = false;
	private Iterator<String> iter = null;

	// 쓰레드 생성시 접속한 클라이언트로부터 데이터를 받거나, 접속한 클라이언트에게 데이터를 전송 하기
	// 위해 클라이언트 소켓을 생성자의 매개변수로 받는다.
	public ServerThread2(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			dis = new DataInputStream(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream());

			int req_packet = 0;
			// 클라이언트가 종료 패킷을 보내지 않으면 반복(계속 패킷과 패킷에 대한
			// 요청 처리를 하겠다)
			while (req_packet != RequestPacket.REQ_EXIT) {

				// 1) 서버는 클라이언트가 보내는 패킷을 받는다.
				req_packet = dis.readInt();
				// 2) 서버는 클라이언트가 보낸 패킷을 분석
				switch (req_packet) {
				case RequestPacket.REQ_LOGIN:

					// 3) 클라이언트가 전송하는 실 데이터를 받아낸다.
					String id = dis.readUTF();
					String pw = dis.readUTF();

					System.out.println(id + "가 로그인 시도");

					boolean isLogin = userDao.login(id, pw);

					// isLogin : true => 로그인 성공
					if (isLogin) {
						session_id = id;
						dos.writeInt(ResponsePacket.RSP_LOGIN_Y);
						System.out.println(id + "가 로그인 완료");

					}
					// isLogin : false => 로그인 실패
					else {
						dos.writeInt(ResponsePacket.RSP_LOGIN_N);
						System.out.println(id + "가 로그인 실패");
					}
					break;
				case RequestPacket.REQ_JOIN:
					// 1) 사용자가 입력한 정보를 받아오기
					String joinid = dis.readUTF();
					String joinpw = dis.readUTF();
					String joinname = dis.readUTF();
					String joinemail = dis.readUTF();

					int joinres = userDao.joinUser(joinid, joinpw, joinname, joinemail);

					if (joinres == 1) {
						System.out.println(joinid + " 회원 가입 성공");
						dos.writeInt(ResponsePacket.RSP_JOIN_Y);
					} else {
						System.out.println(joinid + " 회원 가입 실패");
						dos.writeInt(ResponsePacket.RSP_JOIN_N);
					}

					break;
				// 승완
				case RequestPacket.REQ_BOARD_WRITE:
					String writeTitle = dis.readUTF();
					String writeContents = dis.readUTF();

					int wrtres = boardDao.boardWrite(writeTitle, writeContents, session_id);

					if (wrtres == 1) {
						dos.writeInt(ResponsePacket.RSP_BOARD_WRITE_Y);
					} else {
						dos.writeInt(ResponsePacket.RSP_BOARD_WRITE_N);
					}
					break;
				case RequestPacket.REQ_BOARD_LIST:
					ArrayList<String> board = boardDao.boardView();
					Iterator<String> listIt = board.iterator();
					int listdcnt = board.size();
					if (listdcnt > 0) {
						dos.writeInt(ResponsePacket.RSP_BOARD_LIST_Y);
						
						dos.writeInt(listdcnt);
						while (listIt.hasNext()) {
							dos.writeUTF(listIt.next());
						}
					} else {
						dos.writeInt(ResponsePacket.RSP_BOARD_LIST_N);
					}
					break;
				case RequestPacket.REQ_BOARD_CONTENTS_HAVE:
					idx = dis.readInt();
					if(boardDao.idxCheck(idx)){
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y);
					}else{
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_HAVE_N);
					}
				case RequestPacket.REQ_BOARD_CONTENTS:
					idx = dis.readInt();
					
					if (boardDao.idxCheck(idx)) {
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_Y);
						
						ArrayList<String> res = new ArrayList<>();
						res = boardDao.boardIndexView(idx);
						iter = res.iterator();

						dos.writeInt(res.size());
						
						while (iter.hasNext()) {
							dos.writeUTF(iter.next());
						}
					} else {
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_N);
					}
					break;

				case RequestPacket.REQ_BOARD_MODIFY:
					idx = dis.readInt();
					if (boardDao.idxCheck(idx)) {
						if(boardDao.myBoardCheck(idx, session_id)){
							dos.writeInt(ResponsePacket.RSP_BOARD_MODIFY_Y);
							String modTitle = dis.readUTF();
							String modContents = dis.readUTF();
							boardDao.boardMod(idx, modTitle, modContents);
						}else{
							dos.writeInt(ResponsePacket.RSP_BOARD_MODIFY_N);
						}
						
					}else {
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_N);
					}
					break;

				case RequestPacket.REQ_BOARD_DELETE:
					idx = dis.readInt();
					if (boardDao.idxCheck(idx)) {
						if(boardDao.myBoardCheck(idx, session_id)){
							dos.writeInt(ResponsePacket.RSP_BOARD_DELETE_Y);
							boardDao.boardDel(idx);
						}else{
							dos.writeInt(ResponsePacket.RSP_BOARD_DELETE_N);
						}
						
					}else {
						dos.writeInt(ResponsePacket.RSP_BOARD_CONTENTS_N);
					}
					break;
					
					
				case RequestPacket.REQ_LOGOUT:
					session_id = "";
					dos.writeInt(ResponsePacket.RSP_LOGOUT_Y);
					break;
				}

			}

		} catch (IOException e) {

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dis.close();
				dos.close();
				clientSocket.close();
			} catch (IOException e) {

			}
		}
	}

}

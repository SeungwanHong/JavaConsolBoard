package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;


public class clientMain {
	static final String IP = "192.168.1.2";
	static final int PORT = 8080;
	public static final int MENU_LOGIN = 1;
	public static final int MENU_JOIN = 2;
	public static final int MENU_EXIT = 0;
	public static final int MENU_BOARD_LIST = 1;
	public static final int MENU_BOARD_WRITE = 2;
	public static final int MENU_BOARD_LOGOUT = 3;
	public static final int MENU_BOARD_VIEW = 1;
	public static final int MENU_BOARD_DELET = 2;
	public static final int MENU_BOARD_MODIFY = 3;
	public static final int MENU_BOARD_BACK = 4;
	
	
	static boolean logstate = false;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//1) 서버 접속
		Socket socket = new Socket(IP, PORT);
		//3) 데이터를 받기위한 스트림
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		//2)입력을 받기위한 스케너 생성
		Scanner scan = new Scanner(System.in);
		int menu = -1;
		
		//로그인에 대한 메뉴
		while(menu != MENU_EXIT){
			System.out.println("1)로그인\t2)회원가입\t3)종료");
			System.out.print(">>");
			menu = scan.nextInt();
			scan.nextLine();
			switch(menu){
			case MENU_LOGIN:
				//1) 로그인 패킷을 서버에 전송
				dos.writeInt(RequestPacket.REQ_LOGIN);
				//보낼 데이터를 입력받는다.
				System.out.print("ID : ");
				String id = scan.nextLine();
				System.out.print("PW : ");
				String pw= scan.nextLine();
				//2) 실데이터 전송
				dos.writeUTF(id);
				dos.writeUTF(pw);
				
				//3)서버의 응답을 기다린다.
				int login_rsp = dis.readInt();
				switch(login_rsp){
				case ResponsePacket.RSP_LOGIN_Y:
					System.out.println("로그인 성공");
					logstate = true;
					break;
				case ResponsePacket.RSP_LOGIN_N:
					System.out.println("로그인 실패");
					logstate = false;
					break;
				}
				continue;
			case MENU_JOIN:
				//회원가입 패킷을 서버에 전송
				dos.writeInt(RequestPacket.REQ_JOIN);
				//보낼 데이터 입력
				System.out.print("ID : ");
				String joinid = scan.nextLine();
				System.out.print("PW : ");
				String joinpw= scan.nextLine();
				System.out.print("이름 : ");
				String joinname = scan.nextLine();
				System.out.print("E-Mail : ");
				String joinemail= scan.nextLine();
				//2) 실데이터 전송
				dos.writeUTF(joinid);
				dos.writeUTF(joinpw);
				dos.writeUTF(joinname);
				dos.writeUTF(joinemail);
				//3) 서버의 응답을 기다린다.
				switch(dis.readInt()){
				case ResponsePacket.RSP_JOIN_Y:
					System.out.println("회원갑입 성공");
					break;
				case ResponsePacket.RSP_JOIN_N:
					System.out.println("회원갑입 실패");
					break;
				case ResponsePacket.RSP_JOIN_CHID_N:
					System.out.println("ID 중복");
					break;
				case ResponsePacket.RSP_JOIN_CHPW_N:
					System.out.println("PW가 같지 않습니다.");
					break;
				}
				continue;
			case MENU_EXIT:
				menu = 0;
				dis.close();
				dos.close();
				socket.close();
				continue;
			}
			while(logstate){
				switch(menu){
				case MENU_BOARD_LIST:
				}
				
			}
					
		}
	}
}

package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;

public class ClientMain01 {
	static final String IP = "192.168.1.123";
	static final int PORT = 8080;
	public static final int EXIT = -1;
	public static final int MENU_LOGIN = 1;
	public static final int MENU_JOIN = 2;
	public static final int MENU_EXIT = 3;
	public static final int MENU_BOARD_LIST = 1;
	public static final int MENU_BOARD_WRITE = 2;
	public static final int MENU_BOARD_LOGOUT = 3;
	public static final int MENU_BOARD_VIEW = 1;
	public static final int MENU_BOARD_DELETE = 2;
	public static final int MENU_BOARD_MODIFY = 3;
	public static final int MENU_BOARD_BACK = 4;

	public static boolean logstate = false;
	public static boolean liststate = false;

	public static void main(String[] args) throws UnknownHostException, IOException {

		// 1) 서버 접속
		Socket socket = new Socket(IP, PORT);
		// 3) 데이터를 받기위한 스트림
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		// 2)입력을 받기위한 스케너 생성
		Scanner scan = new Scanner(System.in);
		int menu = 0;

		// 로그인에 대한 메뉴
		while (menu != EXIT) {
			// 로그인 화면 조건
			if (logstate == false) {
				System.out.println("1)로그인\t2)회원가입\t3)종료");
				System.out.print(">>");
				menu = scan.nextInt();
				scan.nextLine();
				switch (menu) {
				case MENU_LOGIN:
					// 1) 로그인 패킷을 서버에 전송
					dos.writeInt(RequestPacket.REQ_LOGIN);
					// 보낼 데이터를 입력받는다.
					System.out.print("ID : ");
					String id = scan.nextLine();
					System.out.print("PW : ");
					String pw = scan.nextLine();
					// 2) 실데이터 전송
					dos.writeUTF(id);
					dos.writeUTF(pw);

					// 3)서버의 응답을 기다린다.
					int login_rsp = dis.readInt();
					switch (login_rsp) {
					case ResponsePacket.RSP_LOGIN_Y:
						System.out.println("로그인 성공");
						logstate = true;
						break;
					case ResponsePacket.RSP_LOGIN_N:
						System.out.println("로그인 실패");
						logstate = false;
						break;
					}
					break;
				case MENU_JOIN:
					// 회원가입 패킷을 서버에 전송
					dos.writeInt(RequestPacket.REQ_JOIN);
					// 보낼 데이터 입력
					System.out.print("ID : ");
					String joinid = scan.nextLine();
					System.out.print("PW : ");
					String joinpw = scan.nextLine();
					System.out.print("이름 : ");
					String joinname = scan.nextLine();
					System.out.print("E-Mail : ");
					String joinemail = scan.nextLine();
					// 2) 실데이터 전송
					dos.writeUTF(joinid);
					dos.writeUTF(joinpw);
					dos.writeUTF(joinname);
					dos.writeUTF(joinemail);
					// 3) 서버의 응답을 기다린다.
					switch (dis.readInt()) {
					case ResponsePacket.RSP_JOIN_Y:
						System.out.println("회원가입 성공");
						break;
					case ResponsePacket.RSP_JOIN_N:
						System.out.println("회원가입 실패");
						break;
					// case ResponsePacket.RSP_JOIN_CHID_N:
					// System.out.println("ID 중복");
					// break;
					// case ResponsePacket.RSP_JOIN_CHPW_N:
					// System.out.println("PW가 같지 않습니다.");
					// break;
					}
					continue;
				case MENU_EXIT:
					menu = EXIT;
					dis.close();
					dos.close();
					socket.close();
					continue;
				}
			} else {
				// 로그인 다음 화면
				if (liststate == false) {
					System.out.println("1) 게시판 목록보기  2) 게시글 추가  3) 로그아웃");
					System.out.print(">>");
					menu = scan.nextInt();
					scan.nextLine();
					int listcnt = 0;
					switch (menu) {
					case MENU_BOARD_LIST:
						dos.writeInt(RequestPacket.REQ_BOARD_LIST);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_LIST_Y:
							listcnt = dis.readInt();
//							System.out.println("글번호     제목          작성자          작성일");
							for (int i = 0; i < listcnt; i++) {
//								System.out.println(dis.readUTF());
								dis.readUTF();
							}
							liststate = true;
							break;
						case ResponsePacket.RSP_BOARD_LIST_N:
							System.out.println("게시글이 없거나, 잘못된 접근");
							liststate = false;
							break;
						}
						break;
					case MENU_BOARD_WRITE:
						dos.writeInt(RequestPacket.REQ_BOARD_WRITE);
						System.out.print("제목 : ");
						String wrtTitle = scan.nextLine();
						System.out.print("내용 : ");
						String wrtContents = scan.nextLine();
						dos.writeUTF(wrtTitle);
						dos.writeUTF(wrtContents);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_WRITE_Y:
							System.out.println("작성 성공");
							break;
						case ResponsePacket.RSP_BOARD_WRITE_N:
							System.out.println("작성 실패");
							break;
						}
						break;
					case MENU_BOARD_LOGOUT:
						dos.writeInt(RequestPacket.REQ_LOGOUT);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_LOGOUT_Y:
							System.out.println("로그아웃 성공");
							logstate = false;
							liststate = false;
							break;
						case ResponsePacket.RSP_LOGOUT_N:
							System.out.println("로그아웃 실패");
							break;
						}
						break;
					}

				}
				// 게시판 목록 보기 이후 메뉴
				else {
//					dos.writeInt(RequestPacket.REQ_BOARD_LIST);
//					int cnt = 0;
//					cnt = dis.readInt();
//					System.out.println(cnt);
//					System.out.println("글번호     제목          작성자          작성일");
//					for (int i = 0; i < cnt; i++) {
//						System.out.println(i);
//						System.out.println(dis.readUTF());
//					}
					dos.writeInt(RequestPacket.REQ_BOARD_LIST);
					int listcnt = 0;
					switch (dis.readInt()) {
					case ResponsePacket.RSP_BOARD_LIST_Y:
						listcnt = dis.readInt();
						System.out.println("글번호 \t제목\t작성자\t작성일");
						for (int i = 0; i < listcnt; i++) {
							System.out.println(dis.readUTF());
						}
						liststate = true;	
						break;
					case ResponsePacket.RSP_BOARD_LIST_N:
						System.out.println("게시글이 없거나, 잘못된 접근");
						liststate = false;
						break;
					}
					System.out.println("1) 게시글 확인  2) 게시글 삭제  3) 게시글 수정 4) 게시판 화면 가기");
					System.out.print(">>");
					menu = scan.nextInt();
					scan.nextLine();

					switch (menu) {
					case MENU_BOARD_VIEW:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS);
						System.out.print("글 번호 : ");
						int contentsIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(contentsIndx);

						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_CONTENTS_Y:
							System.out.println("게시글 보기 성공");
							ArrayList<String> contents = new ArrayList<>();
							int contentsno = dis.readInt();
							for (int i = 0; i < contentsno; i++) {
								contents.add(dis.readUTF());
							}
							System.out.print("글번호  : " + contents.get(0) + "\t제목 : " + contents.get(1));
							System.out.println("\t작성자  : " + contents.get(3) + "\t작성일 : " + contents.get(4)+"\t조회수 : "+contents.get(5));
							System.out.println("내용 : " + contents.get(2));
							break;

						case ResponsePacket.RSP_BOARD_CONTENTS_N:
							System.out.println("게시글 번호를 잘못 입력하였습니다.");
							break;
						}
						break;
					case MENU_BOARD_DELETE:
						System.out.println("삭제할 글번호를 입력하세요");
						int deleteidx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(RequestPacket.REQ_BOARD_MYCONTENTS);
						dos.writeInt(deleteidx);
						
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_MYCONTENTS_Y:
							dos.writeInt(RequestPacket.REQ_BOARD_DELETE);
							dos.writeInt(deleteidx);
							if(dis.readInt() == ResponsePacket.RSP_BOARD_DELETE_Y){
								System.out.println("삭제 성공");
							}
							else if(dis.readInt() == ResponsePacket.RSP_BOARD_DELETE_N){
								//sql문제
								System.out.println("글번호가 잘못됬습니다");
							}
							break;
						case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
							System.out.println("내글이 아니면 삭제 할 수없습니다.");
							break;
						}
						break;
					case MENU_BOARD_MODIFY:
						System.out.println("수정할 글번호를 입력하세요");
						int modifyidx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(RequestPacket.REQ_BOARD_MYCONTENTS);
						dos.writeInt(modifyidx);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_MYCONTENTS_Y:
							dos.writeInt(RequestPacket.REQ_BOARD_MODIFY);
							System.out.print("제목 >> ");
							String modTilte = scan.nextLine();
							System.out.print("내용 >> ");
							String modeContents = scan.nextLine();
							dos.writeInt(modifyidx);
							dos.writeUTF(modTilte);
							dos.writeUTF(modeContents);
							if(dis.readInt() == ResponsePacket.RSP_BOARD_MODIFY_Y){
								System.out.println("수정성공");
							}
							else if(dis.readInt() == ResponsePacket.RSP_BOARD_MODIFY_N){
								System.out.println("글번호가 잘못됬습니다");
							}
							break;
						case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
							System.out.println("내글이 아닙니다.");
							break;
						}
						break;
					case MENU_BOARD_BACK:
						liststate = false;
						logstate = true;
						break;

					}

				}
			}

		}
	}
}
package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;

public class ClientMain02 {
	static final String IP = "192.168.1.187";
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

		Socket socket = new Socket(IP, PORT);
		// 3) 데이터를 받기위한 스트림
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		// 2)입력을 받기위한 스케너 생성
		Scanner scan = new Scanner(System.in);
		//게시판 시작 0, 테스트 시작 -1
		int menu = 0;

		while (menu != EXIT) {
			if (logstate == false) {
				
				}
			} else {
				if (liststate == false) {
					
					}
				} else {
					dos.writeInt(RequestPacket.REQ_BOARD_LIST);
					switch (dis.readInt()) {
					case ResponsePacket.RSP_BOARD_LIST_Y:
						int listcnt = 0;
						listcnt = dis.readInt();
						System.out.println("글번호 \t제목\t작성자\t작성일");
						for (int i = 0; i < listcnt; i++) {
							System.out.println(dis.readUTF());
						}
						break;
					case ResponsePacket.RSP_BOARD_LIST_N:
						System.out.println("게시글이 없거나, 잘못된 접근");
					}
					System.out.println("1) 게시글 확인  2) 게시글 삭제  3) 게시글 수정 4) 게시판 화면 가기");
					System.out.print(">>");
					menu = scan.nextInt();
					scan.nextLine();
					switch (menu) {
					// *sw
					case MENU_BOARD_VIEW:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("확인할 글 번호 : ");
						int contentsIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(contentsIndx);
						if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
//							dos.writeInt(RequestPacket.REQ_BOARD_COUNT_INCREASE);
//							dos.writeInt(contentsIndx);
							dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS);
							dos.writeInt(contentsIndx);
							switch (dis.readInt()) {
							case ResponsePacket.RSP_BOARD_CONTENTS_Y:
								ArrayList<String> contents = new ArrayList<>();
								int contentsno = dis.readInt();
								for (int i = 0; i < contentsno; i++) {
									contents.add(dis.readUTF());
								}
								System.out.println("글번호  : " + contents.get(0) + "\t제목 : " + contents.get(1));
								System.out.println("\t작성자  : " + contents.get(3) + "\t작성일 : " + contents.get(4)+"\t조회수 : "+contents.get(5));
								System.out.println("내용 : " + contents.get(2));
								break;
							case ResponsePacket.RSP_BOARD_CONTENTS_N:
								System.out.println("게시글 번호를 잘못 입력하였습니다.");
								break;
							}

						}else{
							System.out.println("없는 글번호 입니다!!");
						}
						break;
					case MENU_BOARD_DELETE:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("삭제할 글 번호 : ");
						int delIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(delIndx);
						if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
							dos.writeInt(RequestPacket.REQ_BOARD_DELETE);
							dos.writeInt(delIndx);
							switch (dis.readInt()) {
							case ResponsePacket.RSP_BOARD_DELETE_Y:
								System.out.println("삭제 성공");
								break;
							case ResponsePacket.RSP_BOARD_DELETE_N:
								System.out.println("삭제 실패");
								break;
							case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
								System.out.println("내꺼 아님");
								break;
							}
						} else {
							System.out.println("wrong index!!");
						}
						break;
					case MENU_BOARD_MODIFY:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("수정할 글 번호 : ");
						int modIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(modIndx);
						if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
							dos.writeInt(RequestPacket.REQ_BOARD_MODIFY);
							dos.writeInt(modIndx);
							System.out.println("수정할 제목을 입력하세요");
							dos.writeUTF(scan.nextLine());
							System.out.println("수정할 내용을 입력하세요");
							dos.writeUTF(scan.nextLine());
							switch (dis.readInt()) {
							case ResponsePacket.RSP_BOARD_MODIFY_Y:
								System.out.println("수정 성공");
								break;
							case ResponsePacket.RSP_BOARD_MODIFY_N:
								System.out.println("수정 실패");
								break;
							case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
								System.out.println("내꺼 아님");
								break;
							}

						} else {
							System.out.println("wrong index!!");
						}
						break;
					case MENU_BOARD_BACK:
						liststate = false;
					}

				}

			}
		}

	}
}
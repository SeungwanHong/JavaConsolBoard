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
		// 3) �����͸� �ޱ����� ��Ʈ��
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		// 2)�Է��� �ޱ����� ���ɳ� ����
		Scanner scan = new Scanner(System.in);
		//�Խ��� ���� 0, �׽�Ʈ ���� -1
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
						System.out.println("�۹�ȣ \t����\t�ۼ���\t�ۼ���");
						for (int i = 0; i < listcnt; i++) {
							System.out.println(dis.readUTF());
						}
						break;
					case ResponsePacket.RSP_BOARD_LIST_N:
						System.out.println("�Խñ��� ���ų�, �߸��� ����");
					}
					System.out.println("1) �Խñ� Ȯ��  2) �Խñ� ����  3) �Խñ� ���� 4) �Խ��� ȭ�� ����");
					System.out.print(">>");
					menu = scan.nextInt();
					scan.nextLine();
					switch (menu) {
					// *sw
					case MENU_BOARD_VIEW:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("Ȯ���� �� ��ȣ : ");
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
								System.out.println("�۹�ȣ  : " + contents.get(0) + "\t���� : " + contents.get(1));
								System.out.println("\t�ۼ���  : " + contents.get(3) + "\t�ۼ��� : " + contents.get(4)+"\t��ȸ�� : "+contents.get(5));
								System.out.println("���� : " + contents.get(2));
								break;
							case ResponsePacket.RSP_BOARD_CONTENTS_N:
								System.out.println("�Խñ� ��ȣ�� �߸� �Է��Ͽ����ϴ�.");
								break;
							}

						}else{
							System.out.println("���� �۹�ȣ �Դϴ�!!");
						}
						break;
					case MENU_BOARD_DELETE:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("������ �� ��ȣ : ");
						int delIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(delIndx);
						if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
							dos.writeInt(RequestPacket.REQ_BOARD_DELETE);
							dos.writeInt(delIndx);
							switch (dis.readInt()) {
							case ResponsePacket.RSP_BOARD_DELETE_Y:
								System.out.println("���� ����");
								break;
							case ResponsePacket.RSP_BOARD_DELETE_N:
								System.out.println("���� ����");
								break;
							case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
								System.out.println("���� �ƴ�");
								break;
							}
						} else {
							System.out.println("wrong index!!");
						}
						break;
					case MENU_BOARD_MODIFY:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
						System.out.print("������ �� ��ȣ : ");
						int modIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(modIndx);
						if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
							dos.writeInt(RequestPacket.REQ_BOARD_MODIFY);
							dos.writeInt(modIndx);
							System.out.println("������ ������ �Է��ϼ���");
							dos.writeUTF(scan.nextLine());
							System.out.println("������ ������ �Է��ϼ���");
							dos.writeUTF(scan.nextLine());
							switch (dis.readInt()) {
							case ResponsePacket.RSP_BOARD_MODIFY_Y:
								System.out.println("���� ����");
								break;
							case ResponsePacket.RSP_BOARD_MODIFY_N:
								System.out.println("���� ����");
								break;
							case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
								System.out.println("���� �ƴ�");
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
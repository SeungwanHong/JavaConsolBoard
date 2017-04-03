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

		// 1) ���� ����
		Socket socket = new Socket(IP, PORT);
		// 3) �����͸� �ޱ����� ��Ʈ��
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		// 2)�Է��� �ޱ����� ���ɳ� ����
		Scanner scan = new Scanner(System.in);
		int menu = 0;

		// �α��ο� ���� �޴�
		while (menu != EXIT) {
			// �α��� ȭ�� ����
			if (logstate == false) {
				System.out.println("1)�α���\t2)ȸ������\t3)����");
				System.out.print(">>");
				menu = scan.nextInt();
				scan.nextLine();
				switch (menu) {
				case MENU_LOGIN:
					// 1) �α��� ��Ŷ�� ������ ����
					dos.writeInt(RequestPacket.REQ_LOGIN);
					// ���� �����͸� �Է¹޴´�.
					System.out.print("ID : ");
					String id = scan.nextLine();
					System.out.print("PW : ");
					String pw = scan.nextLine();
					// 2) �ǵ����� ����
					dos.writeUTF(id);
					dos.writeUTF(pw);

					// 3)������ ������ ��ٸ���.
					int login_rsp = dis.readInt();
					switch (login_rsp) {
					case ResponsePacket.RSP_LOGIN_Y:
						System.out.println("�α��� ����");
						logstate = true;
						break;
					case ResponsePacket.RSP_LOGIN_N:
						System.out.println("�α��� ����");
						logstate = false;
						break;
					}
					break;
				case MENU_JOIN:
					// ȸ������ ��Ŷ�� ������ ����
					dos.writeInt(RequestPacket.REQ_JOIN);
					// ���� ������ �Է�
					System.out.print("ID : ");
					String joinid = scan.nextLine();
					System.out.print("PW : ");
					String joinpw = scan.nextLine();
					System.out.print("�̸� : ");
					String joinname = scan.nextLine();
					System.out.print("E-Mail : ");
					String joinemail = scan.nextLine();
					// 2) �ǵ����� ����
					dos.writeUTF(joinid);
					dos.writeUTF(joinpw);
					dos.writeUTF(joinname);
					dos.writeUTF(joinemail);
					// 3) ������ ������ ��ٸ���.
					switch (dis.readInt()) {
					case ResponsePacket.RSP_JOIN_Y:
						System.out.println("ȸ������ ����");
						break;
					case ResponsePacket.RSP_JOIN_N:
						System.out.println("ȸ������ ����");
						break;
					// case ResponsePacket.RSP_JOIN_CHID_N:
					// System.out.println("ID �ߺ�");
					// break;
					// case ResponsePacket.RSP_JOIN_CHPW_N:
					// System.out.println("PW�� ���� �ʽ��ϴ�.");
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
				// �α��� ���� ȭ��
				if (liststate == false) {
					System.out.println("1) �Խ��� ��Ϻ���  2) �Խñ� �߰�  3) �α׾ƿ�");
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
//							System.out.println("�۹�ȣ     ����          �ۼ���          �ۼ���");
							for (int i = 0; i < listcnt; i++) {
//								System.out.println(dis.readUTF());
								dis.readUTF();
							}
							liststate = true;
							break;
						case ResponsePacket.RSP_BOARD_LIST_N:
							System.out.println("�Խñ��� ���ų�, �߸��� ����");
							liststate = false;
							break;
						}
						break;
					case MENU_BOARD_WRITE:
						dos.writeInt(RequestPacket.REQ_BOARD_WRITE);
						System.out.print("���� : ");
						String wrtTitle = scan.nextLine();
						System.out.print("���� : ");
						String wrtContents = scan.nextLine();
						dos.writeUTF(wrtTitle);
						dos.writeUTF(wrtContents);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_WRITE_Y:
							System.out.println("�ۼ� ����");
							break;
						case ResponsePacket.RSP_BOARD_WRITE_N:
							System.out.println("�ۼ� ����");
							break;
						}
						break;
					case MENU_BOARD_LOGOUT:
						dos.writeInt(RequestPacket.REQ_LOGOUT);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_LOGOUT_Y:
							System.out.println("�α׾ƿ� ����");
							logstate = false;
							liststate = false;
							break;
						case ResponsePacket.RSP_LOGOUT_N:
							System.out.println("�α׾ƿ� ����");
							break;
						}
						break;
					}

				}
				// �Խ��� ��� ���� ���� �޴�
				else {
//					dos.writeInt(RequestPacket.REQ_BOARD_LIST);
//					int cnt = 0;
//					cnt = dis.readInt();
//					System.out.println(cnt);
//					System.out.println("�۹�ȣ     ����          �ۼ���          �ۼ���");
//					for (int i = 0; i < cnt; i++) {
//						System.out.println(i);
//						System.out.println(dis.readUTF());
//					}
					dos.writeInt(RequestPacket.REQ_BOARD_LIST);
					int listcnt = 0;
					switch (dis.readInt()) {
					case ResponsePacket.RSP_BOARD_LIST_Y:
						listcnt = dis.readInt();
						System.out.println("�۹�ȣ \t����\t�ۼ���\t�ۼ���");
						for (int i = 0; i < listcnt; i++) {
							System.out.println(dis.readUTF());
						}
						liststate = true;	
						break;
					case ResponsePacket.RSP_BOARD_LIST_N:
						System.out.println("�Խñ��� ���ų�, �߸��� ����");
						liststate = false;
						break;
					}
					System.out.println("1) �Խñ� Ȯ��  2) �Խñ� ����  3) �Խñ� ���� 4) �Խ��� ȭ�� ����");
					System.out.print(">>");
					menu = scan.nextInt();
					scan.nextLine();

					switch (menu) {
					case MENU_BOARD_VIEW:
						dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS);
						System.out.print("�� ��ȣ : ");
						int contentsIndx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(contentsIndx);

						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_CONTENTS_Y:
							System.out.println("�Խñ� ���� ����");
							ArrayList<String> contents = new ArrayList<>();
							int contentsno = dis.readInt();
							for (int i = 0; i < contentsno; i++) {
								contents.add(dis.readUTF());
							}
							System.out.print("�۹�ȣ  : " + contents.get(0) + "\t���� : " + contents.get(1));
							System.out.println("\t�ۼ���  : " + contents.get(3) + "\t�ۼ��� : " + contents.get(4)+"\t��ȸ�� : "+contents.get(5));
							System.out.println("���� : " + contents.get(2));
							break;

						case ResponsePacket.RSP_BOARD_CONTENTS_N:
							System.out.println("�Խñ� ��ȣ�� �߸� �Է��Ͽ����ϴ�.");
							break;
						}
						break;
					case MENU_BOARD_DELETE:
						System.out.println("������ �۹�ȣ�� �Է��ϼ���");
						int deleteidx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(RequestPacket.REQ_BOARD_MYCONTENTS);
						dos.writeInt(deleteidx);
						
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_MYCONTENTS_Y:
							dos.writeInt(RequestPacket.REQ_BOARD_DELETE);
							dos.writeInt(deleteidx);
							if(dis.readInt() == ResponsePacket.RSP_BOARD_DELETE_Y){
								System.out.println("���� ����");
							}
							else if(dis.readInt() == ResponsePacket.RSP_BOARD_DELETE_N){
								//sql����
								System.out.println("�۹�ȣ�� �߸�����ϴ�");
							}
							break;
						case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
							System.out.println("������ �ƴϸ� ���� �� �������ϴ�.");
							break;
						}
						break;
					case MENU_BOARD_MODIFY:
						System.out.println("������ �۹�ȣ�� �Է��ϼ���");
						int modifyidx = scan.nextInt();
						scan.nextLine();
						dos.writeInt(RequestPacket.REQ_BOARD_MYCONTENTS);
						dos.writeInt(modifyidx);
						switch (dis.readInt()) {
						case ResponsePacket.RSP_BOARD_MYCONTENTS_Y:
							dos.writeInt(RequestPacket.REQ_BOARD_MODIFY);
							System.out.print("���� >> ");
							String modTilte = scan.nextLine();
							System.out.print("���� >> ");
							String modeContents = scan.nextLine();
							dos.writeInt(modifyidx);
							dos.writeUTF(modTilte);
							dos.writeUTF(modeContents);
							if(dis.readInt() == ResponsePacket.RSP_BOARD_MODIFY_Y){
								System.out.println("��������");
							}
							else if(dis.readInt() == ResponsePacket.RSP_BOARD_MODIFY_N){
								System.out.println("�۹�ȣ�� �߸�����ϴ�");
							}
							break;
						case ResponsePacket.RSP_BOARD_MYCONTENTS_N:
							System.out.println("������ �ƴմϴ�.");
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
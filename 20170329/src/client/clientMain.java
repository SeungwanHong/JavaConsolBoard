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
		
		//1) ���� ����
		Socket socket = new Socket(IP, PORT);
		//3) �����͸� �ޱ����� ��Ʈ��
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		//2)�Է��� �ޱ����� ���ɳ� ����
		Scanner scan = new Scanner(System.in);
		int menu = -1;
		
		//�α��ο� ���� �޴�
		while(menu != MENU_EXIT){
			System.out.println("1)�α���\t2)ȸ������\t3)����");
			System.out.print(">>");
			menu = scan.nextInt();
			scan.nextLine();
			switch(menu){
			case MENU_LOGIN:
				//1) �α��� ��Ŷ�� ������ ����
				dos.writeInt(RequestPacket.REQ_LOGIN);
				//���� �����͸� �Է¹޴´�.
				System.out.print("ID : ");
				String id = scan.nextLine();
				System.out.print("PW : ");
				String pw= scan.nextLine();
				//2) �ǵ����� ����
				dos.writeUTF(id);
				dos.writeUTF(pw);
				
				//3)������ ������ ��ٸ���.
				int login_rsp = dis.readInt();
				switch(login_rsp){
				case ResponsePacket.RSP_LOGIN_Y:
					System.out.println("�α��� ����");
					logstate = true;
					break;
				case ResponsePacket.RSP_LOGIN_N:
					System.out.println("�α��� ����");
					logstate = false;
					break;
				}
				continue;
			case MENU_JOIN:
				//ȸ������ ��Ŷ�� ������ ����
				dos.writeInt(RequestPacket.REQ_JOIN);
				//���� ������ �Է�
				System.out.print("ID : ");
				String joinid = scan.nextLine();
				System.out.print("PW : ");
				String joinpw= scan.nextLine();
				System.out.print("�̸� : ");
				String joinname = scan.nextLine();
				System.out.print("E-Mail : ");
				String joinemail= scan.nextLine();
				//2) �ǵ����� ����
				dos.writeUTF(joinid);
				dos.writeUTF(joinpw);
				dos.writeUTF(joinname);
				dos.writeUTF(joinemail);
				//3) ������ ������ ��ٸ���.
				switch(dis.readInt()){
				case ResponsePacket.RSP_JOIN_Y:
					System.out.println("ȸ������ ����");
					break;
				case ResponsePacket.RSP_JOIN_N:
					System.out.println("ȸ������ ����");
					break;
				case ResponsePacket.RSP_JOIN_CHID_N:
					System.out.println("ID �ߺ�");
					break;
				case ResponsePacket.RSP_JOIN_CHPW_N:
					System.out.println("PW�� ���� �ʽ��ϴ�.");
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
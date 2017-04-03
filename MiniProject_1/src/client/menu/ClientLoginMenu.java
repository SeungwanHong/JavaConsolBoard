package client.menu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;

public class ClientLoginMenu {

	public void showLoginview(Scanner scan, DataOutputStream dos, DataInputStream dis) throws IOException{
		dos.writeInt(RequestPacket.REQ_LOGIN);
		
		System.out.print("ID >> ");
		String id = scan.nextLine();
		
		System.out.print("PW >> ");
		String pw = scan.nextLine();
		
		//2) �� �����͸� ������ �Ѵ�.
		//���̵� ����
		dos.writeUTF(id);
		dos.writeUTF(pw);
		
		//������ ���� �Է��� ���� ���� ���� �ޱ�
		int login_rsp = dis.readInt();
		
		//������ ���� ���� ������ �м�(�α��� ����,���� ����)
		switch(login_rsp){
		case ResponsePacket.RSP_LOGIN_Y:
			System.out.println("�α��� ����");
			break;
		case ResponsePacket.RSP_LOGIN_N:
			System.out.println("�α��� ����");
			break;
		}
	}
}

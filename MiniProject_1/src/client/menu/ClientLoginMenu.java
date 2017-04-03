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
		
		//2) 실 데이터를 보내야 한다.
		//아이디 전송
		dos.writeUTF(id);
		dos.writeUTF(pw);
		
		//서버로 부터 입력한 값에 대한 응답 받기
		int login_rsp = dis.readInt();
		
		//서버로 부터 받은 응답을 분석(로그인 실패,성공 여부)
		switch(login_rsp){
		case ResponsePacket.RSP_LOGIN_Y:
			System.out.println("로그인 성공");
			break;
		case ResponsePacket.RSP_LOGIN_N:
			System.out.println("로그인 실패");
			break;
		}
	}
}

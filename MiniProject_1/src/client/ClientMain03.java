package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import commons.packet.RequestPacket;
import commons.packet.ResponsePacket;
import exception.ExceptionTest;
import exception.MyException;

public class ClientMain03 {
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
      ExceptionTest exceptionTest = new ExceptionTest();

      int menu = 0;

      while (menu != EXIT) {
         if (logstate == false) {
        	 try {
                 System.out.println("1)�α���\t2)ȸ������\t3)����");
                 System.out.print(">>");
                 menu = scan.nextInt();
                 scan.nextLine();
                 exceptionTest.Scanner(menu);
                 break;
              } catch (MyException e) {
                 System.out.println(e.getMessage());
              } catch (InputMismatchException e) {
                 System.out.println("���ڵ� ���� �ȵ�");
                 scan = new Scanner(System.in);
              }

            switch (menu) {
            case MENU_LOGIN:
               dos.writeInt(RequestPacket.REQ_LOGIN);
               System.out.print("id >> ");
               dos.writeUTF(scan.nextLine());
               System.out.print("pw >> ");
               dos.writeUTF(scan.nextLine());
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
               dos.writeInt(RequestPacket.REQ_JOIN);
               System.out.print("id >> ");
               dos.writeUTF(scan.nextLine());
               System.out.print("pw >> ");
               dos.writeUTF(scan.nextLine());
               System.out.print("name >> ");
               dos.writeUTF(scan.nextLine());
               System.out.print("email >> ");
               dos.writeUTF(scan.nextLine());
               switch (dis.readInt()) {
               case ResponsePacket.RSP_JOIN_Y:
                  System.out.println("ȸ������ ����");
                  break;
                  
               case ResponsePacket.RSP_JOIN_N:
                  System.out.println("ȸ������ ����");
                  break;
               }
               break;
               
            case MENU_EXIT:
               dos.writeInt(RequestPacket.REQ_EXIT);
               menu = -1;
               System.out.println("���α׷� ����");
               break;
               default:
            	   System.out.println("�Ŵ��� �ִ� ��ȣ�� �Է��ϼ���");
            	   break;
            }
         } else {
            if (liststate == false) {
            	 try {
                     System.out.println("1) �Խ��� ��Ϻ���  2) �Խñ� �߰�  3) �α׾ƿ�");
                     System.out.print(">>");
                     menu = scan.nextInt();
                     scan.nextLine();
                     exceptionTest.Scanner(menu);
                     break;
                  } catch (MyException e) {
                     System.out.println(e.getMessage());
                  } catch (InputMismatchException e) {
                     System.out.println("���ڵ� ���� �ȵ�");
                     scan = new Scanner(System.in);
                  }

               switch (menu) {
               case MENU_BOARD_LIST:
                  liststate = true;
                  break;
                  
               case MENU_BOARD_WRITE:
                  dos.writeInt(RequestPacket.REQ_BOARD_WRITE);
                  System.out.print("���� : ");
                  dos.writeUTF(scan.nextLine());
                  System.out.print("���� : ");
                  dos.writeUTF(scan.nextLine());
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
                     break;
                     
                  case ResponsePacket.RSP_LOGOUT_N:
                     System.out.println("�α׾ƿ� ����");
                     logstate = true;
                     break;
                  }
                  break;
               default:
            	   System.out.println("�Ŵ��� �ִ� ��ȣ�� �Է��ϼ���");
            	   break;
               }
            } else {
              
                  
            	 try {
                     System.out.println("1) �Խñ� Ȯ��  2) �Խñ� ����  3) �Խñ� ���� 4) �Խ��� ȭ�� ����");
                     System.out.print(">>");
                     menu = scan.nextInt();
                     scan.nextLine();
                     exceptionTest.Scanner(menu);
                     break;
                  } catch (MyException e) {
                     System.out.println(e.getMessage());
                  } catch (InputMismatchException e) {
                     System.out.println("���ڵ� ���� �ȵ�");
                     scan = new Scanner(System.in);
                  }

               switch (menu) {
               // *sw
               case MENU_BOARD_VIEW:
                  dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
                  System.out.print("Ȯ���� �� ��ȣ : ");
                  int contentsIndx = scan.nextInt();
                  scan.nextLine();
                  dos.writeInt(contentsIndx);
                  if (dis.readInt() == ResponsePacket.RSP_BOARD_CONTENTS_HAVE_Y) {
                     // dos.writeInt(RequestPacket.REQ_BOARD_COUNT_INCREASE);
                     // dos.writeInt(contentsIndx);
                     dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS);
                     dos.writeInt(contentsIndx);
                     switch (dis.readInt()) {
                     case ResponsePacket.RSP_BOARD_CONTENTS_Y:
                        ArrayList<String> contents = new ArrayList<>();
                        int contentsno = dis.readInt();
                        for (int i = 0; i < contentsno; i++) {
                           contents.add(dis.readUTF());
                        }
                        
                        System.out.println("----------------------------------------------------");
                        System.out.println("*�۹�ȣ  : " + contents.get(0) + "\t*���� : " + contents.get(1));
                        System.out.println("*�ۼ���  : " + contents.get(3) + "\t*�ۼ��� : " + contents.get(4)
                              + "\t*��ȸ�� : " + contents.get(5));
                        System.out.println("*���� : " + contents.get(2));
                        System.out.println("----------------------------------------------------");
                        break;
                        
                     case ResponsePacket.RSP_BOARD_CONTENTS_N:
                        System.out.println("�Խñ� ��ȣ�� �߸� �Է��Ͽ����ϴ�.");
                        break;
                     }

                  } else {
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
                        System.out.println("�� ���� �ƴմϴ�");
                        break;
                     }
                  } else {
                     System.out.println("�߸��� �� ��ȣ �Դϴ�!!");
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
                        System.out.println("�� ���� �ƴմϴ�");
                        break;
                     }

                  } else {
                     System.out.println("�߸��� �� ��ȣ �Դϴ�!!");
                  }
                  break;
               case MENU_BOARD_BACK:
                  liststate = false;
               default:
            	   System.out.println("�Ŵ��� �ִ� ��ȣ�� �Է��ϼ���");
            	   break;
               }
            }
         }
      }
      socket.close();
      scan.close();

   }
}
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
      // 3) 데이터를 받기위한 스트림
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      // 2)입력을 받기위한 스케너 생성
      Scanner scan = new Scanner(System.in);
      ExceptionTest exceptionTest = new ExceptionTest();

      int menu = 0;

      while (menu != EXIT) {
         if (logstate == false) {
        	 try {
                 System.out.println("1)로그인\t2)회원가입\t3)종료");
                 System.out.print(">>");
                 menu = scan.nextInt();
                 scan.nextLine();
                 exceptionTest.Scanner(menu);
                 break;
              } catch (MyException e) {
                 System.out.println(e.getMessage());
              } catch (InputMismatchException e) {
                 System.out.println("문자도 물론 안됨");
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
                  System.out.println("회원가입 성공");
                  break;
                  
               case ResponsePacket.RSP_JOIN_N:
                  System.out.println("회원가입 실패");
                  break;
               }
               break;
               
            case MENU_EXIT:
               dos.writeInt(RequestPacket.REQ_EXIT);
               menu = -1;
               System.out.println("프로그램 종료");
               break;
               default:
            	   System.out.println("매뉴에 있는 번호만 입력하세요");
            	   break;
            }
         } else {
            if (liststate == false) {
            	 try {
                     System.out.println("1) 게시판 목록보기  2) 게시글 추가  3) 로그아웃");
                     System.out.print(">>");
                     menu = scan.nextInt();
                     scan.nextLine();
                     exceptionTest.Scanner(menu);
                     break;
                  } catch (MyException e) {
                     System.out.println(e.getMessage());
                  } catch (InputMismatchException e) {
                     System.out.println("문자도 물론 안됨");
                     scan = new Scanner(System.in);
                  }

               switch (menu) {
               case MENU_BOARD_LIST:
                  liststate = true;
                  break;
                  
               case MENU_BOARD_WRITE:
                  dos.writeInt(RequestPacket.REQ_BOARD_WRITE);
                  System.out.print("제목 : ");
                  dos.writeUTF(scan.nextLine());
                  System.out.print("내용 : ");
                  dos.writeUTF(scan.nextLine());
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
                     break;
                     
                  case ResponsePacket.RSP_LOGOUT_N:
                     System.out.println("로그아웃 실패");
                     logstate = true;
                     break;
                  }
                  break;
               default:
            	   System.out.println("매뉴에 있는 번호만 입력하세요");
            	   break;
               }
            } else {
              
                  
            	 try {
                     System.out.println("1) 게시글 확인  2) 게시글 삭제  3) 게시글 수정 4) 게시판 화면 가기");
                     System.out.print(">>");
                     menu = scan.nextInt();
                     scan.nextLine();
                     exceptionTest.Scanner(menu);
                     break;
                  } catch (MyException e) {
                     System.out.println(e.getMessage());
                  } catch (InputMismatchException e) {
                     System.out.println("문자도 물론 안됨");
                     scan = new Scanner(System.in);
                  }

               switch (menu) {
               // *sw
               case MENU_BOARD_VIEW:
                  dos.writeInt(RequestPacket.REQ_BOARD_CONTENTS_HAVE);
                  System.out.print("확인할 글 번호 : ");
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
                        System.out.println("*글번호  : " + contents.get(0) + "\t*제목 : " + contents.get(1));
                        System.out.println("*작성자  : " + contents.get(3) + "\t*작성일 : " + contents.get(4)
                              + "\t*조회수 : " + contents.get(5));
                        System.out.println("*내용 : " + contents.get(2));
                        System.out.println("----------------------------------------------------");
                        break;
                        
                     case ResponsePacket.RSP_BOARD_CONTENTS_N:
                        System.out.println("게시글 번호를 잘못 입력하였습니다.");
                        break;
                     }

                  } else {
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
                        System.out.println("내 글이 아닙니다");
                        break;
                     }
                  } else {
                     System.out.println("잘못된 글 번호 입니다!!");
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
                        System.out.println("내 글이 아닙니다");
                        break;
                     }

                  } else {
                     System.out.println("잘못된 글 번호 입니다!!");
                  }
                  break;
               case MENU_BOARD_BACK:
                  liststate = false;
               default:
            	   System.out.println("매뉴에 있는 번호만 입력하세요");
            	   break;
               }
            }
         }
      }
      socket.close();
      scan.close();

   }
}
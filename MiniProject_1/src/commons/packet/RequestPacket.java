package commons.packet;

//클라이언트가 서버에게 전달할 요청패킷 모음
public class RequestPacket {

	// 앞자리가 1 : 회원에 관련된?
	// 두번째가 ~ : 상세 기능(로그인인지 회원가입인지, 수정, 탈퇴~~)
	// 세번째 : 상세 기능에서 더 분리될 내용이 있는가?(임시 자릿수)

	// 단 패킷 값들은 겹치면 안된다
	// 각각의 패킷들은 고유한 값을 가져야 한다.

	public static final int REQ_LOGIN = 100;
	public static final int REQ_JOIN = 110;
	public static final int REQ_LOGOUT = 120;

	public static final int REQ_EXIT = -1;

	public static final int REQ_BOARD_LIST = 200;
	public static final int REQ_BOARD_CONTENTS = 210;
	public static final int REQ_BOARD_CONTENTS_HAVE = 211;
	public static final int REQ_BOARD_COUNT_INCREASE = 212;
	public static final int REQ_BOARD_WRITE = 220;
	public static final int REQ_BOARD_FILE_UPLOAD_Y = 221;
	public static final int REQ_BOARD_FILE_UPLOAD_N = 222;
	public static final int REQ_BOARD_MODIFY = 230;
	public static final int REQ_BOARD_DELETE = 240;
	public static final int REQ_BOARD_MYCONTENTS = 250;
	
	 

	
	

	// ....

}

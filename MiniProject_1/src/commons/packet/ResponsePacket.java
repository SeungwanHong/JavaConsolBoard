package commons.packet;

//서버가 클라이언트의 요청을 처리 한 후 결과값을 응답(Response) 해주기 위한 패킷 모음
public class ResponsePacket {

	public static final int RSP_LOGIN_Y = 101;
	public static final int RSP_LOGIN_N = 102;

	public static final int RSP_LOGOUT_Y = 121;
	public static final int RSP_LOGOUT_N = 122;

	public static final int RSP_JOIN_Y = 111;
	public static final int RSP_JOIN_N = 112;

	public static final int RSP_BOARD_LIST_Y = 201;
	public static final int RSP_BOARD_LIST_N = 202;
	public static final int RSP_BOARD_CONTENTS_Y = 211;
	public static final int RSP_BOARD_CONTENTS_N = 212;
	public static final int RSP_BOARD_CONTENTS_HAVE_Y = 213;
	public static final int RSP_BOARD_CONTENTS_HAVE_N = 214;
	public static final int RSP_BOARD_MYCONTENTS_Y = 215;
	public static final int RSP_BOARD_MYCONTENTS_N = 216;
	public static final int RSP_BOARD_WRITE_Y = 221;
	public static final int RSP_BOARD_WRITE_N = 222;
	public static final int RSP_BOARD_MODIFY_Y = 231;
	public static final int RSP_BOARD_MODIFY_N = 232;
	public static final int RSP_BOARD_DELETE_Y = 241;
	public static final int RSP_BOARD_DELETE_N = 242;
	

}
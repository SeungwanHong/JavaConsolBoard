package commons.packet;

//서버가 클라이언트의 요청을 처리한 후 결과 값을 응답 해주기 위한 패킷 모음
public class ResponsePacket {
	
	public static final int RSP_LOGIN_Y = 101;
	public static final int RSP_LOGIN_N = 102;
	
	public static final int RSP_JOIN_Y = 111;
	public static final int RSP_JOIN_N = 112;
	public static final int RSP_JOIN_CHPW_N = 113;
	public static final int RSP_JOIN_CHID_N = 114;

}

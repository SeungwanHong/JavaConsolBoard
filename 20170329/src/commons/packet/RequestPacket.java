package commons.packet;

//Ŭ���̾�Ʈ�� �������� ������ ��û ��Ŷ ����
public class RequestPacket {
	
	public static final int REQ_EIXT = -1;
	//ȸ�� ���� 1--
	//ȸ�����ó��� ���� ���� 10-, 11-
	//����° : ���� ���п��� �и��� ������ �ִ°�?(�ӽ� �ڸ���)
	
	//�� ��Ŷ�� ���� ��ġ�� �ȵȴ�.
	//������ ��Ŷ���� ������ ���� �������Ѵ�.
	
	public static final int REQ_LOGIN = 100;
	public static final int REQ_JOIN = 110;

	//�Խ��� ���� 2--
	public static final int REQ_BOARD_LIST = 200;
	public static final int REQ_BOARD_MOD = 210;
	
}

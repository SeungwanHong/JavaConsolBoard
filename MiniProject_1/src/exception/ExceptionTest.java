package exception;

public class ExceptionTest {
	public void Scanner(int menu) throws MyException {
		if (menu < 0 && menu > 2147483647) {
			throw new MyException("�Ŵ���ȣ �̿ܿ� ���ڴ� ������ ���� �ʽ��ϴ�..");
		} else {

		}

	}
}
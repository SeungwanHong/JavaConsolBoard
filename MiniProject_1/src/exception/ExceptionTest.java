package exception;

public class ExceptionTest {
	public void Scanner(int menu) throws MyException {
		if (menu < 0 && menu > 2147483647) {
			throw new MyException("매뉴번호 이외에 숫자는 진행이 되지 않습니다..");
		} else {

		}

	}
}
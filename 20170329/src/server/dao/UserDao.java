package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import commons.packet.ResponsePacket;

//MVC 패턴
//Model View Controller

//데이터 베이스에 접근 하여 데이터를 조회, 수정, 삽입, 삭제를 하는 역할을 하는 DAO
//Data Access Object
//세상 세상 중요함
public class UserDao {
	public static final int JOIN_Y = 1;
	public static final int JOIN_N = 2;
	public static final int CHECK_PW_N = 3;
	public static final int CHECK_ID_N = 4;
	
	private final String DB_url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private final String DB_user = "hsw";
	private final String DB_password = "hsw";
	

	private Connection conn;

	public boolean login(String id, String pw) throws SQLException {
		Connection conn = getConnection();
		String sql = "SELECT COUNT(*) AS LOGIN_CNT " + "FROM TB_USER " + "WHERE USERID = ? AND USERPW = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		ResultSet login_rs = pstmt.executeQuery();
		int loginYN = 0;
		while (login_rs.next()) {
			loginYN = login_rs.getInt("LOGIN_CNT");
		}
		conn.close();
		pstmt.close();
		login_rs.close();
		return loginYN == 1;
	}

	public int joinUser(String id, String pw, String ch_pw, String name, String email) throws SQLException {
		Connection conn = getConnection();
		//아이디 채크
		String sql = "SELECT COUNT(*) AS LOGIN_CNT " + "FROM TB_USER " + "WHERE USERID = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);

		ResultSet join_rs = pstmt.executeQuery();
		int idCheckYN = 0;

		join_rs = pstmt.executeQuery();
		
		while (join_rs.next()) {
			idCheckYN = join_rs.getInt("LOGIN_CNT");
		}
		join_rs.close();
		//아이디 중복 체크
		if (idCheckYN == 0) {
			//패스워드 채크 부분
			if (pw.equals(ch_pw)) {
				// 쿼리 처리 insert
				sql = "INSERT INTO TB_USER(USERID,USERPW,USERNAME,EMAIL,JOINDATE) " + "VALUES(?,?,?,?,SYSDATE)";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				pstmt.setString(3, name);
				pstmt.setString(4, email);
				// 예외 처리
				if (pstmt.executeUpdate() == 1) {
					System.out.println("가입 성공");
					conn.close();
					pstmt.close();
					return JOIN_Y;
				} else {
					System.out.println("가입 실패");
					conn.close();
					pstmt.close();
					return JOIN_N;
				}
			} else {
				System.out.println("패스워드가 같지 않습니다.");
				conn.close();
				pstmt.close();
				return CHECK_PW_N;
			}
		} else {
			System.out.println("중복된 아이디가 있습니다.");
			conn.close();
			pstmt.close();
			return CHECK_ID_N;
		}
		
	

	}

	private Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(DB_url, DB_user, DB_password);
		return conn;
	}
}

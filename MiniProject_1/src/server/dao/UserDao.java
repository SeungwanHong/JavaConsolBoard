package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//MVC 패턴
// Model View Controller

// 데이터 베이스에 접근하여 데이터를 조회,수정,삽입,삭제를 하는 역할

//세상세상중요함
//DAO : Data Access Object
public class UserDao {

	private static final String DB_URL = "jdbc:oracle:thin:@192.168.1.123:1521:orcl";
	private static final String DB_USER = "kyh";
	private static final String DB_PASSWORD = "kyh";

	public boolean login(String id, String pw) throws SQLException{
		
		Connection conn = getConnection();
		String sql = "SELECT COUNT(*) AS LOGIN FROM CB_USER "
				+ "WHERE USERID=? AND USERPW=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		
		ResultSet login_rs = pstmt.executeQuery();
		int loginYN = 0;
		if(login_rs.next()){
			loginYN = login_rs.getInt("LOGIN");
		}
		
		login_rs.close();
		pstmt.close();
		conn.close();
		return loginYN == 1;
	}
	public int joinUser(String id, String pw, String name, String email) throws SQLException{
		Connection conn = getConnection();
		String sql = "insert into cb_user(userid,userpw,username,email,joindate)"
				+ " values(?,?, ?, ?, sysdate)";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		pstmt.setString(3, name);
		pstmt.setString(4, email);

		int result = pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		
		return result;
	}
	
	private Connection getConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		return conn;
	}
	
	
	
}

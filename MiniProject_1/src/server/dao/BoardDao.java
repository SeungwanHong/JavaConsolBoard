package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoardDao {

	private static final String DB_URL = "jdbc:oracle:thin:@192.168.1.123:1521:orcl";
	private static final String DB_USER = "kyh";
	private static final String DB_PASSWORD = "kyh";

	private Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		return conn;
	}


	public int boardWrite(String title, String contents, String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert into CB_BOARD(BOARDNO,TITLE,CONTENTS,USERID,WRITEDATE)"
					+ "values(CB_BOARD_SEQ.NEXTVAL,?,?,?,sysdate);";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setString(3, id);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("board write error!!");
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	//파일 업로드
	public int boardWrite(String title, String contents, String id, String file_path) {

		return 0;
	}
	public ArrayList<String> boardView(){
		   
	      ResultSet rs = null;
	      PreparedStatement  pstmt = null;
	      Connection conn = null;
	      ArrayList<String> list = null;
	   
	      try {
	      conn = getConnection();
	      String sql = "SELECT BOARDNO,TITLE,USERID,WRITEDATE "
	            + "FROM CB_BOARD";
	      pstmt = conn.prepareStatement(sql);
	      rs = pstmt.executeQuery();
	      String str="";
	      list = new ArrayList<>();
	      while(rs.next()){
	         String boardno = rs.getString("BOARDNO");
	         String title = rs.getString("TITLE");
	         String userid = rs.getString("USERID");
	         String writedate = rs.getString("WRTITEDATE");
	         
	         str = boardno +"\t"+title+"\t"+userid+"\t"+writedate;
	         list.add(str);}
	      }catch (SQLException e) {
	         System.out.println("boadrView error!!");
	         e.printStackTrace();
	      } finally {
	         try {
	            pstmt.close();
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }

	      }
	      return list;
	   }

	public int boardMod(int index, String title, String contents) {
	      int result = 0;
	      PreparedStatement pstmt = null;
	      Connection conn = null;
	      try {
	         conn = getConnection();

	         String sql = "UPDATE CB_BOARD SET TITLE = ? , CONTENTS = ? WHERE BOARDNO = ? ";
	         pstmt = conn.prepareStatement(sql);

	         pstmt.setString(1, title);
	         pstmt.setString(2, contents);
	         pstmt.setInt(3, index);

	         result = pstmt.executeUpdate(); // 성공이면 1

	      } catch (SQLException e) {
	         System.out.println("boadrModify error!!");
	         e.printStackTrace();
	      } finally {
	         try {
	            pstmt.close();
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }

	      }
	      return result; // 변경성공

	   }

	   public int boardDel(int index){
	      int result = 0;
	      PreparedStatement pstmt = null;
	      Connection conn = null;
	      try{
	       conn = getConnection();

	      String sql = "DELETE FROM CB_BOARD WHERE BOARDNO = ?";
	      pstmt = conn.prepareStatement(sql);

	      pstmt.setInt(1, index);
	      result = pstmt.executeUpdate(); // 성공이면 1

	      
	      }catch (SQLException e) {
	         System.out.println("boadrDelete error!!");
	         e.printStackTrace();
	      } finally {
	         try {
	            pstmt.close();
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }

	      }
	      return result;

	   }

	   public boolean myBoardCheck(int index, String id){
	      ResultSet id_check_rs = null;
	      PreparedStatement pstmt = null;
	      Connection conn = null;
	      String check_id = "";
	      try{
	         conn = getConnection();
	         String sql = "SELECT USERID FROM CB_BOARD WHERE BOARDNO = ? ";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, index);
	         id_check_rs = pstmt.executeQuery();
	         
	         if (id_check_rs.next()) {
	            check_id = id_check_rs.getString("USERID");
	         }
	         
	      } catch (SQLException e) {
	         System.out.println("boadrCheck error!!");
	         e.printStackTrace();
	      } finally {
	         try {
	            id_check_rs.close();
	            pstmt.close();
	            conn.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }

	      }
	      if (id.equals(check_id))
	         return true;
	      else
	         return false;
	   }

	public boolean idxCheck(int index) {
		Connection conn = null;
		ResultSet idx_rs = null;
		PreparedStatement pstmt = null;
		int idxYN = 0;
		try {
			conn = getConnection();
			String idxSql = "SELECT COUNT(*) CNT FROM CB_BOARD " 
						 + "WHERE BOARDNO = ?";
			pstmt = conn.prepareStatement(idxSql);
			pstmt.setInt(1, index);
			idx_rs = pstmt.executeQuery();

			if (idx_rs.next()) {
				idxYN = idx_rs.getInt("CNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				idx_rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return idxYN == 1;
	}

	public ArrayList boardIndexView(int index) throws SQLException {
		Connection conn = getConnection();

		// 글번호, 제목, 내용, 작성자, 작성일
//		String sql2 = ""
				
				
		String sql = "SELECT BOARDNO, TITLE, CONTENTS, USERID,WRITEDATE,HITCOUNT " + 
					 "FROM CB_BOARD WHERE BOARDNO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, index);
		String numStr = "";
		String numstr2 = "";

		ResultSet rs = pstmt.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while (rs.next()) {
			int no = rs.getInt("BOARDNO");
			int no2 = rs.getInt("HITCOUNT");
			numStr = String.valueOf(no);
			numstr2 = String.valueOf(no2);
			
			list.add(numStr);
			list.add(rs.getString("TITLE"));
			list.add(rs.getString("CONTENTS"));
			list.add(rs.getString("USERID"));
			list.add(rs.getString("WRITEDATE"));
			list.add(numstr2);
		}

		pstmt.close();
		conn.close();
		return list;
	}

}

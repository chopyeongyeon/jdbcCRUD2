package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 메인 클래스 입니다
 */
public class Main {

	/**
	 * JDBC 연결 정보 입니다
	 */
	private static final String DB_URL = "";
	// jdbc:oracle:thin:@192.168.10.39:1521/ORCL
	// jdbc:oracle:thin:@localhost:1521:XE
	private static final String DB_USERNAME = "";
	private static final String DB_PASSWORD = "";

	/**
	 * sql 문을 담을 변수입니다
	 */
	private static String sql = "";

	/**
	 * sql 구문을 실행하기 위한 변수 입니다
	 */
	private static PreparedStatement pstmt = null;

	/**
	 * sql 실행 결과를 받아오는 변수 입니다
	 */
	private static ResultSet rs = null;

	/**
	 * 멤버 객체 입니다
	 */
	Member member = new Member();

	/**
	 * 메인 메서드 입니다
	 * @param args 외부로부터 값을 입력 받습니다
	 */
	public static void main(String[] args) {
		try(Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			System.out.println("DB 연결 성공");

			Main main = new Main();

			main.insert(conn, "hi", "1234", "한국");
			main.selectAll(conn);
			main.update(conn, "hi", "5555", "미국");
			main.delete(conn, "hi");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 모든 유저를 불러오는 메서드 입니다
	 * @return 모든 유저 정보
	 */
	private List<Member> selectAll(Connection conn) throws SQLException {

		sql = "select id, pw, name from member";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			List<Member> members = new ArrayList<>();

			try(ResultSet rs = pstmt.executeQuery()) {
				while (rs.next() && rs.isClosed() == false) {
					member.setId(rs.getString("id"));
					member.setPw(rs.getString("pw"));
					member.setName(rs.getString("name"));
					members.add(member);
				}
			}

			return members;
		}
	}

	/**
	 * 멤버를 생성하는 메서드 입니다
	 * @param conn db 접속정보
	 * @param id 생성할 멤버 id 값
	 * @param pw 생성할 멤버 pw 값
	 * @param name 생성할 멤버 name 값
	 * @throws SQLException 쿼리 예외처리
	 */
	private void insert(Connection conn, String id, String pw, String name) throws SQLException {

		sql = "insert into member(id, pw, name) values(?, ?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.executeUpdate();
		}

		try(ResultSet rs = pstmt.getGeneratedKeys()) {

		}

		System.out.println(id + "님이 추가 되었습니다");
	}

	/**
	 * 멤버를 수정하는 메서드 입니다
	 * @param conn db 접속정보
	 * @param id 수정할 멤버 id 값
	 * @param pw 수정할 멤버 pw 값
	 * @param name 수정할 멤버 name 값
	 * @throws SQLException 쿼리 예외처리
	 */
	private void update(Connection conn, String id, String pw, String name) throws SQLException {

		sql = "update member set id = ?, pw = ?, name = ? where id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, id);
			pstmt.executeUpdate();
		}

		try(ResultSet rs = pstmt.getGeneratedKeys()) {

		}

		System.out.println(id + "님이 수정 되었습니다");

	}

	/**
	 * 멤버를 삭제하는 메서드 입니다
	 * @param conn db 접속정보
	 * @param id 삭제할 멤버 id 값
	 * @throws SQLException 쿼리 예외처리
	 */
	private void delete(Connection conn, String id) throws SQLException {

		sql = "delete from member where id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		}

		try(ResultSet rs = pstmt.getGeneratedKeys()) {

		}

		System.out.println(id + "님이 삭제 되었습니다");

	}

	/**
	 * 멤버 클래스 입니다
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	class Member {
		private String id;
		private String pw;
		private String name;

		public Member(String id, String pw, String name) {
			this.id = id;
			this.pw = pw;
			this.name = name;
		}
	}
}
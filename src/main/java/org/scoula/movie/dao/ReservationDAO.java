package org.scoula.movie.dao;

import org.scoula.movie.common.JDBCUtil;
import org.scoula.movie.domain.ReservationVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAO {
    // 공통 DB 연결 객체 가져오기
    private Connection conn = JDBCUtil.getConnection();

    // 1. 전화번호로 예매 내역 조회 쿼리 (팀장님 최신 DATE_FORMAT 및 상태 컬럼 반영)
    private final String RESERVATION_GET_BY_PHONE = """
        SELECT
            r.reservation_id,
            m.movie_title,
            DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
            DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
            r.seat_number,
            p.payment_amount,
            r.reservation_status
        FROM reservation r
        JOIN member mem ON r.member_id = mem.member_id
        JOIN schedule s ON r.schedule_id = s.schedule_id
        JOIN movie m ON s.movie_id = m.movie_id
        JOIN payment p ON r.reservation_id = p.reservation_id
        WHERE mem.phone = ?
        ORDER BY r.reservation_id DESC
        """;

    // 2. 예매번호로 단건 조회 쿼리 (팀장님 최신 DATE_FORMAT 및 상태 컬럼 반영)
    private final String RESERVATION_GET_BY_ID = """
        SELECT
            r.reservation_id,
            m.movie_title,
            DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
            DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
            r.seat_number,
            p.payment_amount,
            r.reservation_status
        FROM reservation r
        JOIN schedule s ON r.schedule_id = s.schedule_id
        JOIN movie m ON s.movie_id = m.movie_id
        JOIN payment p ON r.reservation_id = p.reservation_id
        WHERE r.reservation_id = ?
        """;



    // 3. 예매 취소 쿼리
    private final String RESERVATION_CANCEL_BY_ID = """
        UPDATE reservation
        SET reservation_status = '예매취소'
        WHERE reservation_id = ?
          AND reservation_status = '예매완료'
        """;

    // ResultSet에서 데이터를 뽑아 VO 바구니로 매핑해주는 공통 메서드
    private ReservationVO map(ResultSet rs) throws SQLException {
        ReservationVO vo = new ReservationVO();
        vo.setReservationId(rs.getInt("reservation_id"));
        vo.setMovieTitle(rs.getString("movie_title"));
        vo.setStartTime(rs.getString("start_time"));
        vo.setEndTime(rs.getString("end_time"));
        vo.setSeatNumber(rs.getString("seat_number"));
        vo.setPaymentAmount(rs.getInt("payment_amount"));
        // 👇 팀장님 쿼리에 맞게 상태 매핑 추가!
        vo.setReservationStatus(rs.getString("reservation_status"));
        return vo;
    }

    /**
     * 1. 전화번호로 예매 목록 조회
     */
    public List<ReservationVO> getListByPhone(String phone) throws SQLException {
        List<ReservationVO> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(RESERVATION_GET_BY_PHONE)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /**
     * 2. 예매번호로 단건 조회
     */
    public Optional<ReservationVO> getById(int reservationId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(RESERVATION_GET_BY_ID)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }
        return Optional.empty();
    }


    /**
     * 3. 예매번호로 예매 취소
     * 이미 취소된 예매는 UPDATE 대상이 아니므로 0을 반환한다.
     */
    public int cancelReservation(int reservationId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(RESERVATION_CANCEL_BY_ID)) {
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate();
        }
    }

}
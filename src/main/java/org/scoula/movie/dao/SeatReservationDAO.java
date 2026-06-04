package org.scoula.movie.dao;

import org.scoula.movie.common.JDBCUtil;
import org.scoula.movie.domain.SeatReservationVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatReservationDAO {

    // DB 연결
    private Connection conn = JDBCUtil.getConnection();

    public SeatReservationDAO() {
        if (conn == null) {
            throw new RuntimeException("DB 연결 실패: application.properties를 확인하세요.");
        }
    }

    // 예매 완료된 좌석 조회
    public List<String> getReservedSeats(int scheduleId) throws SQLException {

        String sql = """
                SELECT seat_number
                FROM reservation
                WHERE schedule_id = ?
                AND reservation_status = '예매완료'
                """;

        List<String> seats = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    seats.add(rs.getString("seat_number").toUpperCase());
                }
            }
        }

        return seats;
    }

    // 예매 등록
    public int insertReservation(SeatReservationVO vo) throws SQLException {

        String sql = """
                INSERT INTO reservation
                (member_id, schedule_id, seat_number, reservation_status)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            pstmt.setInt(1, vo.getMemberId());
            pstmt.setInt(2, vo.getScheduleId());
            pstmt.setString(3, vo.getSeatNumber().toUpperCase());
            pstmt.setString(4, vo.getReservationStatus());

            pstmt.executeUpdate();

            // 생성된 예매 번호 반환
            try (ResultSet rs = pstmt.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    // 결제 등록
    public void insertPayment(SeatReservationVO vo) throws SQLException {

        String sql = """
                INSERT INTO payment
                (reservation_id, payment_amount)
                VALUES (?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vo.getReservationId());
            pstmt.setInt(2, vo.getPaymentAmount());

            pstmt.executeUpdate();
        }
    }

    // 좌석 중복 확인
    public boolean isSeatReserved(
            int scheduleId,
            String seatNumber
    ) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE schedule_id = ?
                AND seat_number = ?
                AND reservation_status = '예매완료'
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            pstmt.setString(2, seatNumber.toUpperCase());

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}
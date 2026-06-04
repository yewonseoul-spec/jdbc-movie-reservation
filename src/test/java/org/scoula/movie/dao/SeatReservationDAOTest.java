package org.scoula.movie.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoula.movie.domain.SeatReservationVO;

import java.sql.SQLException;
import java.util.List;

class SeatReservationDAOTest {

    SeatReservationDAO dao = new SeatReservationDAO();

    @Test
    @DisplayName("예매된 좌석 조회")
    void getReservedSeats() throws SQLException {

        int scheduleId = 1;

        List<String> seats = dao.getReservedSeats(scheduleId);

        System.out.println("===== 예매 좌석 =====");

        seats.forEach(System.out::println);
    }

    @Test
    @DisplayName("좌석 중복 확인")
    void isSeatReserved() throws SQLException {

        boolean result = dao.isSeatReserved(1, "A1");

        System.out.println("A1 예매 여부: " + result);
    }

    @Test
    @DisplayName("예매 등록 및 결제 등록")
    void insertReservationAndPayment() throws SQLException {

        String seatNumber = "A2";

        if (dao.isSeatReserved(1, seatNumber)) {
            System.out.println(seatNumber + " 좌석은 이미 예매되었습니다.");
            return;
        }

        SeatReservationVO vo = new SeatReservationVO();

        vo.setMemberId(1);
        vo.setScheduleId(1);
        vo.setSeatNumber(seatNumber);
        vo.setReservationStatus("예매완료");
        vo.setPaymentAmount(15000);

        int reservationId = dao.insertReservation(vo);

        vo.setReservationId(reservationId);

        dao.insertPayment(vo);

        System.out.println("생성된 예매 번호: " + reservationId);
    }
}
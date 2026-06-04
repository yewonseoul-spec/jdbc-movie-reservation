package org.scoula.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservationVO {
    private int reservationId;  // 예매 번호
    private int memberId;       // 회원 번호
    private int scheduleId;     // 상영 번호
    private String seatNumber;  // 좌석 번호
    private String reservationStatus; // 예매 상태
    private int paymentAmount;  // 결제 금액
}
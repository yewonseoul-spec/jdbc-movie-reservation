package org.scoula.movie.domain; // 팀 패키지 경로에 맞게 맞춰줘!

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationVO {
    private int reservationId;     // 예매 번호
    private String movieTitle;     // 영화 제목
    private String startTime;      // 상영 시작 시간
    private String endTime;        // 상영 종료 시간
    private String seatNumber;     // 좌석 번호
    private int paymentAmount;     // 결제 금액

    private String reservationStatus; // 예매 상태 (예매완료/취소)
}
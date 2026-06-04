package org.scoula.movie.domain;

public class SeatReservationVO {
    private int reservationId;
    private int memberId;
    private int scheduleId;
    private String seatNumber;
    private String reservationStatus;
    private int paymentAmount;

    public SeatReservationVO() {
    }

    public SeatReservationVO(int reservationId, int memberId, int scheduleId,
                             String seatNumber, String reservationStatus, int paymentAmount) {
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.seatNumber = seatNumber;
        this.reservationStatus = reservationStatus;
        this.paymentAmount = paymentAmount;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}

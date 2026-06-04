package org.scoula.movie.domain;

public class ReservationVO {
    private int reservationId;
    private String movieTitle;
    private String startTime;
    private String endTime;
    private String seatNumber;
    private int paymentAmount;
    private String reservationStatus;

    public ReservationVO() {
    }

    public ReservationVO(int reservationId, String movieTitle, String startTime, String endTime,
                         String seatNumber, int paymentAmount, String reservationStatus) {
        this.reservationId = reservationId;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatNumber = seatNumber;
        this.paymentAmount = paymentAmount;
        this.reservationStatus = reservationStatus;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Override
    public String toString() {
        return "ReservationVO{" +
                "reservationId=" + reservationId +
                ", movieTitle='" + movieTitle + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", reservationStatus='" + reservationStatus + '\'' +
                '}';
    }
}

package org.scoula.movie.reservation;

import org.scoula.movie.dao.ReservationDAO;
import org.scoula.movie.domain.ReservationVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ReservationView {
    private final Scanner sc;
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public ReservationView(Scanner sc) {
        this.sc = sc;
    }

    public void showReservationAndCancel() {
        System.out.println();
        System.out.println("====== 2. 예매 조회 / 취소 ======");
        System.out.println("0. 이전 메뉴로 돌아가기");
        System.out.println();
        System.out.print("전화번호 입력 >> ");

        String phone = sc.nextLine();

        if (phone.equals("0")) {
            return;
        }

        try {
            List<ReservationVO> reservations = reservationDAO.getListByPhone(phone);

            if (reservations.isEmpty()) {
                System.out.println("해당 전화번호의 예매 내역이 없습니다.");
                waitEnter();
                return;
            }

            printReservationList(reservations);

            System.out.println("취소할 예매번호를 입력하세요.");
            System.out.println("취소하지 않으려면 0을 입력하세요.");
            System.out.print("예매번호 입력 >> ");

            int reservationId = inputNumber();

            if (reservationId == 0) {
                return;
            }

            boolean myReservation = false;
            for (ReservationVO reservation : reservations) {
                if (reservation.getReservationId() == reservationId) {
                    myReservation = true;
                    break;
                }
            }

            if (!myReservation) {
                System.out.println("방금 조회한 예매 목록에 없는 예매번호입니다.");
                waitEnter();
                return;
            }

            cancelReservation(reservationId);
        } catch (SQLException e) {
            System.out.println("예매 조회/취소 중 오류가 발생했습니다.");
            e.printStackTrace();
            waitEnter();
        }
    }

    private void cancelReservation(int reservationId) throws SQLException {
        Optional<ReservationVO> optionalReservation = reservationDAO.getById(reservationId);

        if (optionalReservation.isEmpty()) {
            System.out.println("해당 예매번호의 예매 내역이 없습니다.");
            waitEnter();
            return;
        }

        ReservationVO reservation = optionalReservation.get();

        System.out.println();
        System.out.println("====== 취소할 예매 정보 ======");
        printReservation(reservation);

        if (!"예매완료".equals(reservation.getReservationStatus())) {
            System.out.println("이미 취소되었거나 취소할 수 없는 예매입니다.");
            waitEnter();
            return;
        }

        System.out.print("정말 취소하시겠습니까? (Y/N) >> ");
        String answer = sc.nextLine();

        if (!answer.equalsIgnoreCase("Y")) {
            System.out.println("예매 취소를 취소했습니다.");
            waitEnter();
            return;
        }

        int count = reservationDAO.cancelReservation(reservationId);

        if (count == 1) {
            System.out.println("예매가 취소되었습니다.");
        } else {
            System.out.println("예매 취소에 실패했습니다. 이미 취소된 예매일 수 있습니다.");
        }

        waitEnter();
    }

    private void printReservationList(List<ReservationVO> reservations) {
        System.out.println();
        System.out.println("====== 예매 내역 ======");

        for (ReservationVO reservation : reservations) {
            printReservation(reservation);
            System.out.println("----------------------------");
        }
    }

    private void printReservation(ReservationVO reservation) {
        System.out.println("예매번호 : " + reservation.getReservationId());
        System.out.println("영화명 : " + reservation.getMovieTitle());
        System.out.println("상영시간 : " + reservation.getStartTime() + " ~ " + reservation.getEndTime());
        System.out.println("좌석 : " + reservation.getSeatNumber());
        System.out.println("결제금액 : " + reservation.getPaymentAmount());
        System.out.println("예매상태 : " + reservation.getReservationStatus());
    }

    private int inputNumber() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("숫자를 입력해주세요 >> ");
            }
        }
    }

    private void waitEnter() {
        System.out.println();
        System.out.println("Enter : 초기 메뉴로 돌아가기");
        sc.nextLine();
    }
}

package org.scoula.movie.view;

import org.scoula.movie.dao.SeatReservationDAO;
import org.scoula.movie.domain.SeatReservationVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class SeatReservationView {

    private final Scanner sc;
    private final SeatReservationDAO dao = new SeatReservationDAO();

    private final int TICKET_PRICE = 15000;
    private final List<String> validSeats = createValidSeats();

    public SeatReservationView(Scanner sc) {
        this.sc = sc;
    }

    public void reserveSeat(int memberId, int scheduleId) {
        while (true) {
            try {
                List<String> reservedSeats = dao.getReservedSeats(scheduleId);

                printSeatMap(reservedSeats);

                System.out.println();
                System.out.println("========== 좌석 선택 ==========");
                System.out.println("0. 이전 메뉴로 돌아가기");
                System.out.println();
                System.out.print("좌석 입력 >> ");

                String input = sc.nextLine()
                        .toUpperCase()
                        .replace(" ", "");

                if (input.equals("0")) {
                    return;
                }

                if (input.isEmpty()) {
                    System.out.println("좌석을 입력하세요.");
                    continue;
                }

                List<String> selectedSeats = Arrays.asList(input.split(","));

                if (hasInvalidSeat(selectedSeats)) {
                    System.out.println("존재하지 않는 좌석입니다.");
                    continue;
                }

                if (hasDuplicateSeat(selectedSeats)) {
                    System.out.println("같은 좌석을 중복 선택할 수 없습니다.");
                    continue;
                }

                if (hasReservedSeat(selectedSeats, reservedSeats)) {
                    System.out.println("이미 예매된 좌석이 포함되어 있습니다.");
                    continue;
                }

                int totalAmount = selectedSeats.size() * TICKET_PRICE;

                printPaymentConfirm(selectedSeats, totalAmount);

                String answer = sc.nextLine();

                if (answer.equals("0")) {
                    System.out.println("결제가 취소되었습니다.");
                    continue;
                }

                if (!answer.equals("1")) {
                    System.out.println("잘못된 입력입니다.");
                    continue;
                }

                List<Integer> reservationIds = new ArrayList<>();

                for (String seat : selectedSeats) {
                    SeatReservationVO vo = new SeatReservationVO();

                    vo.setMemberId(memberId);
                    vo.setScheduleId(scheduleId);
                    vo.setSeatNumber(seat);
                    vo.setReservationStatus("예매완료");
                    vo.setPaymentAmount(TICKET_PRICE);

                    int reservationId = dao.insertReservation(vo);
                    vo.setReservationId(reservationId);

                    dao.insertPayment(vo);
                    reservationIds.add(reservationId);
                }

                printComplete(reservationIds, scheduleId, selectedSeats, totalAmount);

                sc.nextLine();
                return;

            } catch (Exception e) {
                System.out.println("예매 처리 중 오류가 발생했습니다.");
                e.printStackTrace();
                return;
            }
        }
    }

    private List<String> createValidSeats() {
        List<String> seats = new ArrayList<>();

        for (char row = 'A'; row <= 'E'; row++) {
            for (int num = 1; num <= 5; num++) {
                seats.add(row + String.valueOf(num));
            }
        }

        return seats;
    }

    private void printSeatMap(List<String> reservedSeats) {
        System.out.println();
        System.out.println("========== SCREEN ==========");
        System.out.println();
        System.out.println("상영관 : 1관");
        System.out.println();

        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print("[" + row + "열] ");

            for (int num = 1; num <= 5; num++) {
                String seat = row + String.valueOf(num);

                if (reservedSeats.contains(seat)) {
                    System.out.print("[X] ");
                } else {
                    System.out.print(seat + " ");
                }
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("[X] : 예매 불가 좌석");
    }

    private void printPaymentConfirm(List<String> selectedSeats, int totalAmount) {
        System.out.println();
        System.out.println("========== 결제 확인 ==========");
        System.out.println("선택 좌석 : " + String.join(", ", selectedSeats)
                + " (총 " + selectedSeats.size() + "매)");
        System.out.println("총 결제 금액 : " + String.format("%,d", totalAmount) + "원");
        System.out.println();
        System.out.println("1. 결제 진행");
        System.out.println("0. 결제 취소");
        System.out.print("메뉴 선택 >> ");
    }

    private void printComplete(
            List<Integer> reservationIds,
            int scheduleId,
            List<String> selectedSeats,
            int totalAmount
    ) {
        System.out.println();
        System.out.println("========== 예매 완료 ==========");
        System.out.println("예매가 완료되었습니다.");
        System.out.println();
        System.out.println("예매 번호 : " + reservationIds);
        System.out.println("상영 번호 : " + scheduleId);
        System.out.println("좌석 번호 : " + String.join(", ", selectedSeats)
                + " (총 " + selectedSeats.size() + "매)");
        System.out.println("결제 금액 : " + String.format("%,d", totalAmount) + "원");
        System.out.println();
        System.out.println("Enter : 초기 메뉴로 돌아가기");
    }

    private boolean hasInvalidSeat(List<String> selectedSeats) {
        for (String seat : selectedSeats) {
            if (!validSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasReservedSeat(List<String> selectedSeats, List<String> reservedSeats) {
        for (String seat : selectedSeats) {
            if (reservedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDuplicateSeat(List<String> selectedSeats) {
        return selectedSeats.size() != new HashSet<>(selectedSeats).size();
    }
}
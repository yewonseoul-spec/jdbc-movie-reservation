package org.scoula.movie.main;

import org.scoula.movie.member.MemberView;
import org.scoula.movie.movie.MovieView;
import org.scoula.movie.reservation.ReservationView;

import java.util.Scanner;

public class MainMenuView {
    private final Scanner sc = new Scanner(System.in);
    private final MovieView movieView = new MovieView(sc);
    private final ReservationView reservationView = new ReservationView(sc);
    private final MemberView memberView = new MemberView(sc);

    public void run() {
        while (true) {
            printMainMenu();
            int menu = inputMenu();

            switch (menu) {
                case 1 -> movieView.showShowingMovies();
                case 2 -> reservationView.showReservationAndCancel();
                case 3 -> memberView.searchMember();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("0~3 사이 숫자를 입력해주세요.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("========== 영화 예매 시스템 ==========");
        System.out.println("1. 상영중 영화 조회");
        System.out.println("2. 예매 조회 / 취소");
        System.out.println("3. 회원 정보 조회");
        System.out.println("0. 프로그램 종료");
        System.out.println("====================================");
        System.out.print("메뉴 선택 >> ");
    }

    private int inputMenu() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("숫자를 입력해주세요 >> ");
            }
        }
    }
}

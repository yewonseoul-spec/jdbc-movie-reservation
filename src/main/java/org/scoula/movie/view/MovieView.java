package org.scoula.movie.view;

import org.scoula.movie.dao.MemberDAO;
import org.scoula.movie.dao.MovieDAO;
import org.scoula.movie.domain.MovieScheduleVO;
import org.scoula.movie.domain.MovieVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MovieView {
    private final Scanner sc;
    private final MovieDAO movieDAO = new MovieDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final SeatReservationView seatReservationView;

    public MovieView(Scanner sc) {
        this.sc = sc;
        this.seatReservationView = new SeatReservationView(sc);
    }

    public void showShowingMovies() {
        System.out.println();
        System.out.println("========== 1. 상영중 영화 조회 ==========");

        try {
            List<MovieVO> movies = movieDAO.getShowingMovies();

            if (movies.isEmpty()) {
                System.out.println("오늘 상영중인 영화가 없습니다.");
                waitEnterToMain();
                return;
            }

            for (MovieVO movie : movies) {
                System.out.println(movie.getMovieId() + ". " + movie.getMovieTitle());
            }

            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.println();
            System.out.print("상영 시간을 조회할 영화 번호 입력 >> ");

            int movieId = inputNumber();

            if (movieId == 0) {
                return;
            }

            printSchedules(movieId);

        } catch (SQLException e) {
            System.out.println("상영중 영화 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
            waitEnterToMain();
        }
    }

    private void printSchedules(int movieId) throws SQLException {
        List<MovieScheduleVO> schedules = movieDAO.getSchedulesByMovieId(movieId);

        System.out.println();
        System.out.println("========== 상영 시간 목록 ==========");

        if (schedules.isEmpty()) {
            System.out.println("해당 영화의 오늘 상영 시간이 없습니다.");
            waitEnterToMain();
            return;
        }

        for (MovieScheduleVO schedule : schedules) {
            System.out.println("상영번호: " + schedule.getScheduleId());
            System.out.println("영화명: " + schedule.getMovieTitle());
            System.out.println("시간: " + schedule.getStartTime() + " ~ " + schedule.getEndTime());
            System.out.println("----------------------------");
        }

        System.out.println("0. 이전 메뉴로 돌아가기");
        System.out.println();
        System.out.print("예매할 상영번호 입력 >> ");

        int scheduleId = inputNumber();

        if (scheduleId == 0) {
            return;
        }

        if (!isValidScheduleId(schedules, scheduleId)) {
            System.out.println("목록에 없는 상영번호입니다.");
            waitZeroToBack();
            return;
        }

        System.out.print("회원번호 입력 >> ");
        int memberId = inputNumber();

        if (!memberDAO.existsById(memberId)) {
            System.out.println();
            System.out.println("존재하지 않는 회원번호입니다.");
            System.out.println("회원 정보 조회 후 다시 예매해주세요.");
            waitZeroToBack();
            return;
        }

        seatReservationView.reserveSeat(memberId, scheduleId);
    }

    private boolean isValidScheduleId(List<MovieScheduleVO> schedules, int scheduleId) {
        for (MovieScheduleVO schedule : schedules) {
            if (schedule.getScheduleId() == scheduleId) {
                return true;
            }
        }
        return false;
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

    private void waitZeroToBack() {
        System.out.println();
        System.out.println("0. 이전 메뉴로 돌아가기");

        while (true) {
            System.out.print("메뉴 선택 >> ");
            int menu = inputNumber();

            if (menu == 0) {
                return;
            }

            System.out.println("0을 입력해주세요.");
        }
    }

    private void waitEnterToMain() {
        System.out.println();
        System.out.println("Enter : 초기 메뉴로 돌아가기");
        sc.nextLine();
    }
}
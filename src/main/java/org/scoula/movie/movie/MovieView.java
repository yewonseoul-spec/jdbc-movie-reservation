package org.scoula.movie.movie;

import org.scoula.movie.dao.MovieDAO;
import org.scoula.movie.domain.MovieScheduleVO;
import org.scoula.movie.domain.MovieVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MovieView {
    private final Scanner sc;
    private final MovieDAO movieDAO = new MovieDAO();

    public MovieView(Scanner sc) {
        this.sc = sc;
    }

    public void showShowingMovies() {
        while (true) {
            System.out.println();
            System.out.println("====== 1. 상영중 영화 조회 ======");

            try {
                List<MovieVO> movies = movieDAO.getShowingMovies();

                if (movies.isEmpty()) {
                    System.out.println("오늘 상영중인 영화가 없습니다.");
                    waitEnter();
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
                waitEnter();
                return;
            }
        }
    }

    private void printSchedules(int movieId) throws SQLException {
        List<MovieScheduleVO> schedules = movieDAO.getSchedulesByMovieId(movieId);

        System.out.println();
        System.out.println("====== 상영 시간 목록 ======");

        if (schedules.isEmpty()) {
            System.out.println("해당 영화의 오늘 상영 시간이 없습니다.");
            waitEnter();
            return;
        }

        for (MovieScheduleVO schedule : schedules) {
            System.out.println("상영번호: " + schedule.getScheduleId());
            System.out.println("영화명: " + schedule.getMovieTitle());
            System.out.println("시간: " + schedule.getStartTime() + " ~ " + schedule.getEndTime());
            System.out.println("----------------------------");
        }

        System.out.println("상영번호(schedule_id)는 이후 좌석 선택/예매 기능에 넘겨주면 됩니다.");
        waitEnter();
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
        System.out.println("Enter : 이전 메뉴로 돌아가기");
        sc.nextLine();
    }
}

package org.scoula.movie.dao;

import org.scoula.movie.common.JDBCUtil;
import org.scoula.movie.domain.MovieScheduleVO;
import org.scoula.movie.domain.MovieVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private final Connection conn = JDBCUtil.getConnection();

    private static final String SELECT_SHOWING_MOVIES = """
            SELECT DISTINCT
                m.movie_id,
                m.movie_title
            FROM movie m
            JOIN schedule s
              ON m.movie_id = s.movie_id
            WHERE DATE(s.start_time) = CURDATE()
            ORDER BY m.movie_id
            """;

    private static final String SELECT_SCHEDULES_BY_MOVIE_ID = """
            SELECT
                s.schedule_id,
                m.movie_title,
                DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
                DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time
            FROM schedule s
            JOIN movie m
              ON s.movie_id = m.movie_id
            WHERE s.movie_id = ?
              AND DATE(s.start_time) = CURDATE()
            ORDER BY s.start_time
            """;

    public List<MovieVO> getShowingMovies() throws SQLException {
        List<MovieVO> movies = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SHOWING_MOVIES);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MovieVO vo = new MovieVO();
                vo.setMovieId(rs.getInt("movie_id"));
                vo.setMovieTitle(rs.getString("movie_title"));
                movies.add(vo);
            }
        }

        return movies;
    }

    public List<MovieScheduleVO> getSchedulesByMovieId(int movieId) throws SQLException {
        List<MovieScheduleVO> schedules = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SCHEDULES_BY_MOVIE_ID)) {
            pstmt.setInt(1, movieId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MovieScheduleVO vo = new MovieScheduleVO();
                    vo.setScheduleId(rs.getInt("schedule_id"));
                    vo.setMovieTitle(rs.getString("movie_title"));
                    vo.setStartTime(rs.getString("start_time"));
                    vo.setEndTime(rs.getString("end_time"));
                    schedules.add(vo);
                }
            }
        }

        return schedules;
    }
}

package org.scoula.movie.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoula.movie.domain.ReservationVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

class ReservationDAOTest {

    // 우리가 만든 일꾼 객체 불러오기
    ReservationDAO dao = new ReservationDAO();

    @Test
    @DisplayName("전화번호로 예매 목록을 조회한다.")
    void getListByPhone() throws SQLException {
        // 팀장님이 더미 데이터로 넣어둔 김철수의 전화번호
        String testPhone = "010-1234-5678";

        List<ReservationVO> list = dao.getListByPhone(testPhone);
        System.out.println("=== 전화번호 조회 결과 ===");
        for (ReservationVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("예매번호로 단건 조회를 한다.")
    void getById() throws SQLException {
        // 팀장님이 더미 데이터로 넣어둔 예매번호
        int testId = 1001;

        Optional<ReservationVO> vo = dao.getById(testId);
        System.out.println("=== 예매번호 단건 조회 결과 ===");

        // 데이터가 있으면 콘솔에 출력!
        vo.ifPresent(System.out::println);
    }
}
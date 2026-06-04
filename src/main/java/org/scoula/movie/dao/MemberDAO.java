package org.scoula.movie.dao;

import org.scoula.movie.common.JDBCUtil;
import org.scoula.movie.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    public MemberVO findByPhone(String phone) {
        String sql = "SELECT member_id, member_name, phone FROM member WHERE phone = ?";
        MemberVO memberVO = null;

        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                memberVO = new MemberVO(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("phone")
                );
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberVO;
    }

    public boolean existsById(int memberId) {
        String sql = "SELECT COUNT(*) FROM member WHERE member_id = ?";

        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
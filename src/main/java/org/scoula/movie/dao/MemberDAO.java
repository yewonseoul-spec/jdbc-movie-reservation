package org.scoula.movie.dao;

import org.scoula.movie.common.JDBCUtil;
import org.scoula.movie.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public MemberVO findByPhone(String phone) {

        String sql = "SELECT member_id, member_name, phone FROM member WHERE phone = ?";
        //System.out.println("=========== " + conn);
        MemberVO memberVO = null;
        try {
            conn = JDBCUtil.getConnection();

            //System.out.println("=========== " + conn);

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                memberVO = new MemberVO(rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                //JDBCUtil.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return memberVO;
    }
}
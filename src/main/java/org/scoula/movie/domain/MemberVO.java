package org.scoula.movie.domain;

public class MemberVO {

    private int member_id;
    private String member_name;
    private String phone;

    public MemberVO() {
    }

    public MemberVO(int memberId, String memberName, String phone) {
        this.member_id = memberId;
        this.member_name = memberName;
        this.phone = phone;
    }

    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int memberId) {
        this.member_id = memberId;
    }

    public String getMemberName() {
        return member_name;
    }

    public void setMemberName(String memberName) {
        this.member_name = memberName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
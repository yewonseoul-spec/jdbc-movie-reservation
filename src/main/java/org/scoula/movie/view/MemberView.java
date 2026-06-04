package org.scoula.movie.view;

import org.scoula.movie.dao.MemberDAO;
import org.scoula.movie.domain.MemberVO;

import java.util.Scanner;

public class MemberView {

    private final Scanner sc;
    private final MemberDAO dao = new MemberDAO();

    public MemberView(Scanner sc) {
        this.sc = sc;
    }

    public void searchMember() {
        System.out.println();
        System.out.println("========== 3. 회원 정보 조회 ==========");
        System.out.println();
        System.out.println("0. 이전 메뉴로 돌아가기");
        System.out.println();

        while (true) {
            System.out.print("전화번호 입력 >> ");

            String phone = formatPhone(sc.nextLine());

            if (phone.equals("0")) {
                return;
            }

            MemberVO member = dao.findByPhone(phone);

            if (member == null) {
                System.out.println();
                System.out.println("입력한 전화번호와 일치하는 회원이 없습니다.");
                System.out.println("다시 입력하세요.");
                System.out.println();
                continue;
            }

            printMember(member);
            return;
        }
    }

    private String formatPhone(String phone) {
        if (phone.equals("0")) {
            return phone;
        }

        phone = phone.replace("-", "").trim();

        if (phone.length() == 11) {
            phone = phone.replaceFirst(
                    "(\\d{3})(\\d{4})(\\d{4})",
                    "$1-$2-$3"
            );
        }

        return phone;
    }

    private void printMember(MemberVO member) {
        System.out.println();
        System.out.println("========== 회원 정보 조회 결과 ==========");
        System.out.println();

        System.out.println("[회원 정보]");
        System.out.println();

        System.out.println("회원 번호 : " + member.getMemberId());
        System.out.println();

        System.out.println("이름 : " + member.getMemberName());
        System.out.println();

        System.out.println("전화번호 : " + member.getPhone());
        System.out.println();

        System.out.println("----------------------------");
        System.out.println();
        System.out.println("Enter : 초기 메뉴로 돌아가기");

        sc.nextLine();
    }
}
-- movie_reservation_flow_project_schema.sql
-- 현재 GitHub 프로젝트(jdbc-movie-reservation) 안의 docs/sql/moviedb.sql 구조에 맞춘 버전
-- DBMS: MySQL 8.x 기준
-- DB 이름은 application.properties의 url=jdbc:mysql://127.0.0.1:3308/moviedb 에 맞춰 moviedb 사용

DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE moviedb;

-- =========================
-- 1. 테이블 생성
-- 현재 프로젝트의 단순 스키마 기준
-- =========================

CREATE TABLE member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    member_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE movie (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_title VARCHAR(100) NOT NULL
);

CREATE TABLE schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
);

CREATE TABLE reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    schedule_id INT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    reservation_status VARCHAR(20) NOT NULL DEFAULT '예매완료',
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id),
    INDEX idx_reservation_member(member_id),
    INDEX idx_reservation_schedule(schedule_id),
    INDEX idx_reservation_status(reservation_status)
);

CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    payment_amount INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    INDEX idx_payment_reservation(reservation_id)
);

-- =========================
-- 2. 테스트 데이터
-- =========================

INSERT INTO member(member_name, phone)
VALUES
('김철수', '010-1234-5678'),
('이영희', '010-2222-3333');

INSERT INTO movie(movie_title)
VALUES
('범죄도시4'),
('파묘'),
('인사이드 아웃2');

INSERT INTO schedule(movie_id, start_time, end_time)
VALUES
(1, CONCAT(CURDATE(), ' 12:30:00'), CONCAT(CURDATE(), ' 14:19:00')),
(1, CONCAT(CURDATE(), ' 17:00:00'), CONCAT(CURDATE(), ' 18:49:00')),
(2, CONCAT(CURDATE(), ' 13:00:00'), CONCAT(CURDATE(), ' 15:14:00')),
(3, CONCAT(CURDATE(), ' 16:00:00'), CONCAT(CURDATE(), ' 17:36:00'));

-- 흐름도 예시처럼 예매번호 1001부터 시작
ALTER TABLE reservation AUTO_INCREMENT = 1001;

-- 예시 예매 데이터: 김철수 / 범죄도시4 / 첫 번째 상영 / B4 / 12,000원
INSERT INTO reservation(member_id, schedule_id, seat_number, reservation_status)
VALUES (1, 1, 'B4', '예매완료');

INSERT INTO payment(reservation_id, payment_amount)
VALUES (1001, 12000);

-- =========================
-- 3. 화면 흐름별 SQL
-- =========================

-- ======================================================
-- [초기 메뉴]
-- 초기 메뉴 자체는 SQL이 아니라 Java에서 출력합니다.
-- 1. 상영중 영화 조회
-- 2. 예매 조회 / 취소
-- 3. 회원 정보 조회
-- 0. 프로그램 종료
-- ======================================================


-- ======================================================
-- [1. 상영중 영화 조회]
-- 오늘 상영 스케줄이 있는 영화 목록 조회
-- 현재 프로젝트 스키마에는 movie_status 컬럼이 없으므로
-- schedule에 오늘 날짜가 존재하는 영화를 '상영중/상영예정 영화'로 봅니다.
-- ======================================================

SELECT DISTINCT
    m.movie_id,
    m.movie_title
FROM movie m
JOIN schedule s
  ON m.movie_id = s.movie_id
WHERE DATE(s.start_time) = CURDATE()
ORDER BY m.movie_id;


-- ======================================================
-- [1-* 영화 선택 후] 오늘 상영 시간 조회
-- PreparedStatement에서는 s.movie_id = ? 로 사용
-- ======================================================

/*
SELECT
    s.schedule_id,
    DATE_FORMAT(s.start_time, '%H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%H:%i') AS end_time
FROM schedule s
WHERE s.movie_id = ?
  AND DATE(s.start_time) = CURDATE()
ORDER BY s.start_time;
*/

-- Workbench 테스트용: movie_id = 1
SELECT
    s.schedule_id,
    DATE_FORMAT(s.start_time, '%H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%H:%i') AS end_time
FROM schedule s
WHERE s.movie_id = 1
  AND DATE(s.start_time) = CURDATE()
ORDER BY s.start_time;


-- ======================================================
-- [좌석 선택 화면]
-- 현재 프로젝트 스키마에는 seat 테이블이 없으므로
-- A1~A4, B1~B4 좌석 목록을 SQL 안에서 임시로 만들어 사용합니다.
-- 이미 예매완료인 좌석은 x, 아니면 좌석명 출력
-- PreparedStatement에서는 r.schedule_id = ? 로 사용
-- ======================================================

/*
SELECT
    seats.seat_row,
    seats.seat_col,
    CASE
        WHEN r.reservation_id IS NOT NULL THEN 'x'
        ELSE seats.seat_number
    END AS display_seat
FROM (
    SELECT 'A' AS seat_row, 1 AS seat_col, 'A1' AS seat_number
    UNION ALL SELECT 'A', 2, 'A2'
    UNION ALL SELECT 'A', 3, 'A3'
    UNION ALL SELECT 'A', 4, 'A4'
    UNION ALL SELECT 'B', 1, 'B1'
    UNION ALL SELECT 'B', 2, 'B2'
    UNION ALL SELECT 'B', 3, 'B3'
    UNION ALL SELECT 'B', 4, 'B4'
) seats
LEFT JOIN reservation r
  ON r.schedule_id = ?
 AND r.seat_number = seats.seat_number
 AND r.reservation_status = '예매완료'
ORDER BY seats.seat_row, seats.seat_col;
*/

-- Workbench 테스트용: schedule_id = 1
SELECT
    seats.seat_row,
    seats.seat_col,
    CASE
        WHEN r.reservation_id IS NOT NULL THEN 'x'
        ELSE seats.seat_number
    END AS display_seat
FROM (
    SELECT 'A' AS seat_row, 1 AS seat_col, 'A1' AS seat_number
    UNION ALL SELECT 'A', 2, 'A2'
    UNION ALL SELECT 'A', 3, 'A3'
    UNION ALL SELECT 'A', 4, 'A4'
    UNION ALL SELECT 'B', 1, 'B1'
    UNION ALL SELECT 'B', 2, 'B2'
    UNION ALL SELECT 'B', 3, 'B3'
    UNION ALL SELECT 'B', 4, 'B4'
) seats
LEFT JOIN reservation r
  ON r.schedule_id = 1
 AND r.seat_number = seats.seat_number
 AND r.reservation_status = '예매완료'
ORDER BY seats.seat_row, seats.seat_col;


-- ======================================================
-- [결제 진행 화면]
-- 현재 schedule 테이블에 price 컬럼이 없으므로 티켓 가격은 12,000원으로 고정 계산합니다.
-- 실제 Java에서는 선택 좌석 수 * 12000 으로 계산해도 됩니다.
-- ======================================================

-- Workbench 테스트용: 선택 좌석 2개라고 가정
SELECT
    2 AS selected_seat_count,
    12000 AS ticket_price,
    2 * 12000 AS total_payment_amount;


-- ======================================================
-- [예매 완료 처리]
-- 현재 프로젝트 스키마는 reservation 테이블에 seat_number가 1개만 들어가는 구조입니다.
-- 그래서 여러 좌석을 한 예매번호에 묶는 구조는 아니고,
-- 좌석 1개 기준 INSERT 예시만 제공합니다.
-- PreparedStatement에서는 member_id, schedule_id, seat_number를 바인딩합니다.
-- ======================================================

/*
START TRANSACTION;

-- 1) 이미 예매된 좌석인지 확인
SELECT reservation_id
FROM reservation
WHERE schedule_id = ?
  AND seat_number = ?
  AND reservation_status = '예매완료'
FOR UPDATE;

-- 2) 예매 생성
INSERT INTO reservation(member_id, schedule_id, seat_number, reservation_status)
VALUES (?, ?, ?, '예매완료');

-- 3) 결제 생성
INSERT INTO payment(reservation_id, payment_amount)
VALUES (LAST_INSERT_ID(), 12000);

COMMIT;
*/


-- ======================================================
-- [2-1. 예매 조회] 전화번호로 예매 조회
-- PreparedStatement에서는 mem.phone = ? 로 사용
-- ======================================================

/*
SELECT
    r.reservation_id,
    m.movie_title,
    DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
    r.seat_number,
    p.payment_amount,
    r.reservation_status
FROM reservation r
JOIN member mem
  ON r.member_id = mem.member_id
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE mem.phone = ?
ORDER BY r.reservation_id DESC;
*/

-- Workbench 테스트용
SELECT
    r.reservation_id,
    m.movie_title,
    DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
    r.seat_number,
    p.payment_amount,
    r.reservation_status
FROM reservation r
JOIN member mem
  ON r.member_id = mem.member_id
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE mem.phone = '010-1234-5678'
ORDER BY r.reservation_id DESC;


-- ======================================================
-- [2-1. 예매 조회] 예매번호로 예매 조회
-- PreparedStatement에서는 r.reservation_id = ? 로 사용
-- ======================================================

/*
SELECT
    r.reservation_id,
    m.movie_title,
    DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
    r.seat_number,
    p.payment_amount,
    r.reservation_status
FROM reservation r
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE r.reservation_id = ?;
*/

-- Workbench 테스트용
SELECT
    r.reservation_id,
    m.movie_title,
    DATE_FORMAT(s.start_time, '%Y-%m-%d %H:%i') AS start_time,
    DATE_FORMAT(s.end_time, '%Y-%m-%d %H:%i') AS end_time,
    r.seat_number,
    p.payment_amount,
    r.reservation_status
FROM reservation r
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE r.reservation_id = 1001;


-- ======================================================
-- [2-2. 예매 취소] 취소 확인 화면용 조회
-- PreparedStatement에서는 r.reservation_id = ? 로 사용
-- ======================================================

/*
SELECT
    r.reservation_id,
    m.movie_title,
    r.seat_number,
    p.payment_amount AS refund_amount,
    r.reservation_status
FROM reservation r
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE r.reservation_id = ?
  AND r.reservation_status = '예매완료';
*/

-- Workbench 테스트용
SELECT
    r.reservation_id,
    m.movie_title,
    r.seat_number,
    p.payment_amount AS refund_amount,
    r.reservation_status
FROM reservation r
JOIN schedule s
  ON r.schedule_id = s.schedule_id
JOIN movie m
  ON s.movie_id = m.movie_id
JOIN payment p
  ON r.reservation_id = p.reservation_id
WHERE r.reservation_id = 1001
  AND r.reservation_status = '예매완료';


-- ======================================================
-- [2-2. 예매 취소] 실제 취소 처리
-- 현재 프로젝트 payment 테이블에는 payment_status 컬럼이 없으므로
-- 결제 상태 UPDATE는 하지 않고 reservation_status만 '예매취소'로 변경합니다.
-- 전체 스크립트 실행하자마자 예시 예매가 취소되지 않도록 주석 처리했습니다.
-- ======================================================

/*
START TRANSACTION;

SELECT reservation_status
FROM reservation
WHERE reservation_id = ?
FOR UPDATE;

UPDATE reservation
SET reservation_status = '예매취소'
WHERE reservation_id = ?
  AND reservation_status = '예매완료';

COMMIT;
*/

-- Workbench 테스트용: 예매번호 1001 취소
/*
START TRANSACTION;

SELECT reservation_status
FROM reservation
WHERE reservation_id = 1001
FOR UPDATE;

UPDATE reservation
SET reservation_status = '예매취소'
WHERE reservation_id = 1001
  AND reservation_status = '예매완료';

COMMIT;

SELECT
    r.reservation_id,
    r.reservation_status
FROM reservation r
WHERE r.reservation_id = 1001;
*/


-- ======================================================
-- [3. 회원 정보 조회]
-- PreparedStatement에서는 phone = ? 로 사용
-- ======================================================

/*
SELECT
    member_id,
    member_name,
    phone
FROM member
WHERE phone = ?;
*/

-- Workbench 테스트용
SELECT
    member_id,
    member_name,
    phone
FROM member
WHERE phone = '010-1234-5678';

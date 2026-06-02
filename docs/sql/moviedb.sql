CREATE DATABASE moviedb;
USE moviedb;

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
                             reservation_status VARCHAR(20) NOT NULL,
                             FOREIGN KEY (member_id) REFERENCES member(member_id),
                             FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);

CREATE TABLE payment (
                         payment_id INT AUTO_INCREMENT PRIMARY KEY,
                         reservation_id INT NOT NULL,
                         payment_amount INT NOT NULL,
                         FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id)
);
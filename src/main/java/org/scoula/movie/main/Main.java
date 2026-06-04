package org.scoula.movie.main;

import org.scoula.movie.seat.SeatReservationView;

public class Main {

    public static void main(String[] args) {

        SeatReservationView view =
                new SeatReservationView();

        view.reserveSeat(1, 1);
    }
}
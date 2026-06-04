package org.scoula.movie.domain;

public class MovieScheduleVO {
    private int scheduleId;
    private String movieTitle;
    private String startTime;
    private String endTime;

    public MovieScheduleVO() {
    }

    public MovieScheduleVO(int scheduleId, String movieTitle, String startTime, String endTime) {
        this.scheduleId = scheduleId;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

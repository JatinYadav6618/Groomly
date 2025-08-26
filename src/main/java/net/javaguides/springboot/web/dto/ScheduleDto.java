package net.javaguides.springboot.web.dto;

import net.javaguides.springboot.model.ScheduleStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ScheduleDto {
    private Long id;
    private String barberEmail;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private String date;
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH-mm")
    private String time;
    private String status;

    public ScheduleDto(Long id, String barberEmail, String date,
                       String time, String status) {
        this.id = id;
        this.barberEmail = barberEmail;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

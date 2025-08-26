package net.javaguides.springboot.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="barber_id")
    private User barber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private BarberServiceModel service;

    private LocalDate date;

    @DateTimeFormat(pattern = "HH-mm")
    private String time;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('CONFIRMED', 'CANCELLED') " +
            "DEFAULT " +
            "'CONFIRMED'")
    private BookingStatus status;

    public Booking() {
    }

    public Booking(User client, User barber,
                   BarberServiceModel service, LocalDate date,
                   String time) {
        this.client = client;
        this.barber = barber;
        this.service = service;
        this.date = date;
        this.time = time;
    }

    public Booking(LocalDate date, String time) {
        this.date=date;
        this.time=time;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getBarber() {
        return barber;
    }

    public void setBarber(User barber) {
        this.barber = barber;
    }

    public BarberServiceModel getService() {
        return service;
    }

    public void setService(BarberServiceModel service) {
        this.service = service;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "client=" + client +
                ", barber=" + barber +
                ", service=" + service +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}

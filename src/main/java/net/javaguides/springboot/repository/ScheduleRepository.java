package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.ScheduleStatus;
import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();

    List<Schedule> findByBarberOrderByDateAscTimeAsc(User barber);
    Optional<Schedule> findById(Long Id);

    Optional<Schedule> findByDateAndTime(LocalDate date,
                                         LocalTime time);
    @Query("SELECT DISTINCT s.date FROM Schedule s WHERE s.status = 'FREE'")
    List<LocalDate> findDistinctDates();

    @Query("SELECT DISTINCT s.time FROM Schedule s WHERE s.status = 'FREE'")
    List<LocalTime> findDistinctTimes();

    @Query("SELECT DISTINCT s.date FROM Schedule s WHERE s.barber =" +
            " :barber AND s.status = :bookingStatus")
    List<LocalDate> findDistinctDatesByBarberAndBookingStatus(@Param(
            "barber") User barber, @Param("bookingStatus") ScheduleStatus bookingStatus);

    @Query("SELECT DISTINCT s.time FROM Schedule s WHERE s.barber =" +
            " :barber AND s.status = :bookingStatus AND s.date= " +
            ":date")
    List<LocalTime> findDistinctTimeByBarberAndBookingStatusAndDate(@Param(
            "barber") User barber,
                                                                    @Param("bookingStatus") ScheduleStatus bookingStatus, @Param("date") LocalDate date);
}

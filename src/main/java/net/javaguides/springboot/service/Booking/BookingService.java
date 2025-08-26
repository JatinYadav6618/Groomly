package net.javaguides.springboot.service.Booking;

import net.javaguides.springboot.model.Booking;
import net.javaguides.springboot.web.dto.BookingDto;

import java.util.List;

public interface BookingService {
    List<Booking> findAll();
    Booking findById(Long id);
    List<Booking> findBookingsByClient();
    List<Booking> findBookingsByCurrentBarber();
    List<BookingDto> findBookingsWithDetails();
    void cancel(Booking booking);
    Booking edit(BookingDto bookingDto, Long id);
    Booking save(BookingDto bookingDto);
}

package net.javaguides.springboot.service.Booking;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.*;
import net.javaguides.springboot.repository.BarberServiceRepositry;
import net.javaguides.springboot.repository.BookingRepository;
import net.javaguides.springboot.repository.ScheduleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.BarberService.BarberService;
import net.javaguides.springboot.service.EmailSender.NotificationService;
import net.javaguides.springboot.service.User.UserService;
import net.javaguides.springboot.web.dto.BookingDto;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService notificationService;
    private final BarberService barberService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              UserService userService, ScheduleRepository scheduleRepository, NotificationService notificationService, BarberService barberService){
        this.bookingRepository=bookingRepository;
        this.userRepository=userRepository;
        this.userService = userService;
        this.scheduleRepository=scheduleRepository;
        this.notificationService = notificationService;
        this.barberService = barberService;
    }
    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(()-> new AppException("Booking not found"));
    }

    @Override
    public List<Booking> findBookingsByClient() {
        User loggedInUser= userService.getLoggedInUser();
        return(bookingRepository.findBookingsByClient(loggedInUser));
    }
    public List<Booking> findBookingsByCurrentBarber() {
        User loggedInUser= userService.getLoggedInUser();
        return(bookingRepository.findBookingsByBarber(loggedInUser));
    }

    @Override
    public List<BookingDto> findBookingsWithDetails() {
        List<Booking> bookings=this.bookingRepository.findAll();
        return convertToDtos(bookings);
    }

    private List<BookingDto> convertToDtos(List<Booking> bookings) {
        // Convert the booking entities to DTOs
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(booking.getId());
            bookingDto.setDate(booking.getDate());
            bookingDto.setTime(booking.getTime());
            bookingDto.setClientEmail(booking.getClient().getEmail());
            bookingDto.setBarberEmail(booking.getBarber().getEmail());
            bookingDto.setServiceName(booking.getService().getName());
            // Set other necessary fields
            bookingDtos.add(bookingDto);
        }
        return bookingDtos;
    }

    @Override
    public Booking save(BookingDto bookingDto) {
        Booking booking= new Booking(
                bookingDto.getDate(),
                bookingDto.getTime()
        );
        booking.setStatus(BookingStatus.CONFIRMED);
        setScheduleStatus(booking, ScheduleStatus.BUSY);
        getBooking(bookingDto, booking);
        notificationService.sendMail(booking.getClient().getEmail(),
                new String[]{},
                "Booking Successful", "You have successfully booked" +
                        " a Groomly experience at "+booking.getTime()+", barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName());
        notificationService.sendMail(booking.getClient().getEmail(),
                new String[]{},
                "Booking Successful", booking.getClient().getEmail()+
                " successfully booked" +
                        " a Groomly experience at "+booking.getTime()+", barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName());
        notificationService.sendMessage(booking.getClient().getPhoneNumber(),booking.getClient().getEmail()+
                " successfully booked" +
                " a Groomly experience at "+booking.getTime()+", " +
                "barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName());
        notificationService.sendMessage(booking.getBarber().getPhoneNumber()
                ,booking.getClient().getEmail()+
                "successfully booked" +
                " a Groomly experience at "+booking.getTime()+", " +
                "barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName());
        return bookingRepository.save(booking);

    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        setScheduleStatus(booking,ScheduleStatus.FREE);
        String barberNotificationBody="You have " +
                "successfully cancelled the Groomly " +
                "booking experience  " +
                " at "+booking.getTime()+", barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName();
        String clientNotificationBody=booking.getClient().getEmail()+
                " successfully cancelled the " +
                " Groomly experience at "+booking.getTime()+", " +
                "barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName();
        notificationService.sendMail(booking.getClient().getEmail(),
                new String[]{},
                "Booking Cancelled successfully",
                barberNotificationBody);
        notificationService.sendMail(booking.getClient().getEmail()
                ,new String[]{}, "Booking Cancelled Successful", clientNotificationBody
                );
        notificationService.sendMessage(booking.getClient().getPhoneNumber(),barberNotificationBody);
        notificationService.sendMessage(booking.getBarber().getPhoneNumber(),clientNotificationBody);
        bookingRepository.save(booking);
    }

    @Override
    public Booking edit(BookingDto bookingDto, Long id) throws AppException{
        Booking previousBooking =
                this.findById(id);
        Booking booking = new Booking(
                bookingDto.getDate(),
                bookingDto.getTime()
        );
        booking.setId(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        setScheduleStatus(booking,  ScheduleStatus.BUSY);
        setScheduleStatus(previousBooking,  ScheduleStatus.FREE);
        getBooking(bookingDto, booking);
        String barberNotificationBody="You have " +
                "successfully edited the Groomly " +
                "booking experience  " +
                " at "+booking.getTime()+", barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName();
        String clientNotificationBody=booking.getClient().getEmail()+
                " successfully edited the " +
                " Groomly experience at "+booking.getTime()+", " +
                "barber: "+ booking.getBarber().getEmail()+" for service: " +booking.getService().getName();
        notificationService.sendMail(booking.getClient().getEmail(),
                new String[]{},
                "Booking Updated successfully",
                barberNotificationBody);
        notificationService.sendMail(booking.getClient().getEmail()
                ,new String[]{}, "Booking Updated Successful",
                clientNotificationBody
        );
        notificationService.sendMessage(booking.getClient().getPhoneNumber(),barberNotificationBody);
        notificationService.sendMessage(booking.getBarber().getPhoneNumber(),clientNotificationBody);
        return bookingRepository.save(booking);
    }

    private void getBooking(BookingDto bookingDto, Booking booking) {
        booking.setBarber(userRepository.findByEmail(bookingDto.getBarberEmail()));
        booking.setService(barberService.findById(bookingDto.getServiceId()));
        booking.setClient(userService.getLoggedInUser());
    }

    private void setScheduleStatus(Booking booking,
                                   ScheduleStatus scheduleStatus) throws AppException{
        Schedule schedule=
                scheduleRepository.findByDateAndTime(booking.getDate(), LocalTime.parse(booking.getTime(), DateTimeFormatter.ofPattern("HH:mm"))).orElseThrow(()-> new AppException("Schedule not found"));
        schedule.setStatus(scheduleStatus);
    }
}

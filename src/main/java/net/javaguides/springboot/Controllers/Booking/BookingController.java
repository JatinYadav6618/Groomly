package net.javaguides.springboot.Controllers.Booking;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.*;
import net.javaguides.springboot.repository.BarberServiceRepositry;
import net.javaguides.springboot.repository.BookingRepository;
import net.javaguides.springboot.repository.ScheduleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.BarberService.BarberService;
import net.javaguides.springboot.service.Booking.BookingService;
import net.javaguides.springboot.service.EmailSender.NotificationService;
import net.javaguides.springboot.service.User.UserService;
import net.javaguides.springboot.web.dto.BookingDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BarberServiceRepositry barberServiceRepositry;
    private final UserService userService;
    private final BarberService barberService;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService emailService;

    public BookingController(BookingService bookingService,
                             BookingRepository bookingRepository,
                             UserRepository userRepository,
                             BarberServiceRepositry barberServiceRepositry, UserService userService, BarberService barberService, ScheduleRepository scheduleRepository, NotificationService emailService){
        this.bookingService=bookingService;
        this.bookingRepository=bookingRepository;
        this.userRepository=userRepository;
        this.barberServiceRepositry=barberServiceRepositry;
        this.userService=userService;
        this.barberService=barberService;
        this.scheduleRepository = scheduleRepository;
        this.emailService = emailService;
    }

    @ModelAttribute("bookingDto")
    public BookingDto bookingDto(){
        return new BookingDto();
    }

    @ModelAttribute("barberList")
    public List<User> getBarberList(){return userService.listUsersByRole(RoleName.ROLE_BARBER);}
    @ModelAttribute("serviceList")
    public List<BarberServiceModel> getServiceList(){return barberService.listBarberServices();}
    @ModelAttribute("freeDatesList")
    public List<LocalDate> getDateList(){return scheduleRepository.findDistinctDates();}
    @ModelAttribute("freeTimeList")
    public List<LocalTime> getTimeList(){return scheduleRepository.findDistinctTimes();}

    @GetMapping
    public String showBookingForm() { return "booking";}
    private Long selectedBarberId;
    private String selectedDate;
    private String selectedTimeSlot;

    @GetMapping("/step1")
    public String showStep1() {
        return "Booking/booking-step1";
    }

    @PostMapping("/step2")
    public String processStep2(@RequestParam("barber") Long barberId) {
        selectedBarberId = barberId;
        return "redirect:/booking/step2";
    }
    @GetMapping("/step2")
    public String showStep2(Model model) {
        List<LocalDate> availableDates =
                scheduleRepository.findDistinctDatesByBarberAndBookingStatus(userRepository.findById(selectedBarberId).orElseThrow(()->new AppException("Barber not found")), ScheduleStatus.FREE);
        model.addAttribute("availableDates", availableDates);
        return "Booking/booking-step2";
    }
    @PostMapping("/step3")
    public String processStep3(@RequestParam("date") String date) {
        System.out.println(date);
        selectedDate = date;
        System.out.println(selectedDate);
        return "redirect:/booking/step3";
    }

    @GetMapping("/step3")
    public String showStep3(Model model) {
        List<LocalTime> availableTimeSlots =
                scheduleRepository.findDistinctTimeByBarberAndBookingStatusAndDate(userRepository.findById(selectedBarberId).orElseThrow(()->new AppException("Barber not found")), ScheduleStatus.FREE, LocalDate.parse(selectedDate));
        model.addAttribute("availableTimeSlots", availableTimeSlots);
        return "Booking/booking-step3";
    }

    @GetMapping("/step4")
    public String showStep4(Model model) {
        List<LocalTime> availableTimeSlots =
                scheduleRepository.findDistinctTimeByBarberAndBookingStatusAndDate(userRepository.findById(selectedBarberId).orElseThrow(()->new AppException("Barber not found")), ScheduleStatus.FREE, LocalDate.parse(selectedDate));
        model.addAttribute("availableTimeSlots", availableTimeSlots);
        return "Booking/booking-step4";
    }

    @PostMapping("/step4")
    public String confirmBooking(@RequestParam("time") String timeSlot, @RequestParam("serviceId") Long serviceId, Model model) {
        selectedTimeSlot = timeSlot;
        // Process the booking and save it to the database
        User barber=
                userRepository.findById(selectedBarberId).orElseThrow(()->new AppException("Barber not found"));
        BookingDto bookingDto= new BookingDto(barber.getEmail(),
                serviceId,
                LocalDate.parse(selectedDate),selectedTimeSlot);
        bookingService.save(bookingDto);
        model.addAttribute("booking",bookingDto);
        return "redirect:/booking/client/my-bookings?success";
    }

    @GetMapping("/confirmation")
    public String showConfirmation(Model model) {
        User selectedBarber=
                userRepository.findById(selectedBarberId).orElseThrow(()->new AppException("Barber not found"));;
        model.addAttribute("selectedBarber", selectedBarber);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("selectedTimeSlot", selectedTimeSlot);
        return "Booking/booking-step4";
    }
    @PostMapping
    public String book(@ModelAttribute("bookingDto") BookingDto bookingDto){
        bookingService.save(bookingDto);
        return "redirect:/booking?success";
    }
    @GetMapping("/all")
    public String showBookingList(Model model){
        List<Booking> bookingList= bookingRepository.findAll();
        model.addAttribute("bookings", bookingList);
        return "bookings";
    }
    @GetMapping("/client/my-bookings")
    public String showBookingListByClient(Model model){
        List<Booking> bookingList= bookingService.findBookingsByClient();
        model.addAttribute("bookings", bookingList);
        return "bookings";
    }
    @GetMapping("/barber/my-bookings")
    public String showBookingListByBarber(Model model){
        List<Booking> bookingList=
                bookingService.findBookingsByCurrentBarber();
        model.addAttribute("bookings", bookingList);
        return "bookings";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id,
                               Model model){
        Booking booking=
                bookingRepository.findById(id).orElseThrow(()-> new AppException(
                        "Booking with booking id not found"+id));
        BookingDto bookingDto =
                new BookingDto(
                        booking.getBarber().getEmail(),
                        booking.getService().getId(),
                        booking.getDate(),booking.getTime());
        model.addAttribute("booking", booking);
        model.addAttribute("bookingDto", bookingDto);
        return "update-booking";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable("id") long id,
                                BookingDto bookingDto){
        bookingService.edit(bookingDto,id);
        return "redirect:/booking/edit/{id}?edit";
    }
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable("id") long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid booking Id:" + id));
        bookingService.cancel(booking);
        return "redirect:/booking/client/my-bookings?cancel";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid booking Id:" + id));
        bookingService.cancel(booking);
        bookingRepository.delete(booking);
        return "redirect:/booking/all?delete";
    }
}

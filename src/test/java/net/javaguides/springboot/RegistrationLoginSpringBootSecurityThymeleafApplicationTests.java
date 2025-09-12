package net.javaguides.springboot;

import net.javaguides.springboot.model.Booking;
import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.ScheduleStatus;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.BookingRepository;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.ScheduleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.Booking.BookingService;
import net.javaguides.springboot.service.EmailSender.NotificationService;
import net.javaguides.springboot.service.Schedule.ScheduleService;
import net.javaguides.springboot.service.User.UserService;
import net.javaguides.springboot.web.dto.BookingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class RegistrationLoginSpringBootSecurityThymeleafApplicationTests {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private NotificationService emailSenderService;

//
//	@Test
//	public void listScheduleByDay(){
//		List<Booking> bookings =
//				bookingRepository.findBookingsByDate(LocalDate.parse("2025-10-25",
//						DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//		System.out.println(bookings);
//	}
//
//
//	@Test
//	public void findScheduleByDateAndTime(){
//		BookingDto bookingDto = new BookingDto(
//                "naman@gmail.com",1L,LocalDate.parse("2025-09-11",
//				DateTimeFormatter.ofPattern("yyyy-MM-dd")),"10:00");
//		bookingService.save(bookingDto);
//	}
//
//	@Test
//	public void findScheduleByDateAndTimeAndBarber(){
//		User barber= userRepository.findByEmail("naman@gmail.com");
//		List<LocalTime> scheduleList=
//				scheduleRepository.findDistinctTimeByBarberAndBookingStatusAndDate(barber, ScheduleStatus.FREE,LocalDate.parse("2025-11" +
//						"-25"));
//		System.out.println(scheduleList);
//
//	}
//
//	@Test
//	public void sendMessage(){
//		emailSenderService.sendMessage("1234567890", "Hey");
//	}
//
//	@Test
//	public void verifyPhoneNumber(){
//		emailSenderService.verifyPhoneNumber("1234567890");
//	}

	@Test
	public void generateSlots() {
		LocalDate localDate = LocalDate.parse("2025-09-11");

		List<Schedule> slots = scheduleService.createSlotsForDay(6L, localDate,
				LocalTime.of(10, 0), LocalTime.of(18, 0), 30);

		System.out.println(slots);
	}

}

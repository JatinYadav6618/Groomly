package net.javaguides.springboot;

import net.javaguides.springboot.model.Booking;
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


	@Test
	public void listScheduleByDay(){
		List<Booking> bookings =
				bookingRepository.findBookingsByDate(LocalDate.parse("2023-10-22",
						DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		System.out.println(bookings);
	}


	@Test
	public void findScheduleByDateAndTime(){
		BookingDto bookingDto = new BookingDto(
                "mukichig@africau.edu",2L,LocalDate.parse("2023-11" +
						"-25",
				DateTimeFormatter.ofPattern("yyyy-MM-dd")),"09:00");
		bookingService.save(bookingDto);
	}

	@Test
	public void findScheduleByDateAndTimeAndBarber(){
		User barber= userRepository.findByEmail("kfana@africau.edu");
		List<LocalTime> scheduleList=
				scheduleRepository.findDistinctTimeByBarberAndBookingStatusAndDate(barber, ScheduleStatus.FREE,LocalDate.parse("2023-11" +
						"-25"));
		System.out.println(scheduleList);

	}

	@Test
	public void sendMessage(){
		emailSenderService.sendMessage("+263777344737", "Hey");
	}

	@Test
	public void verifyPhoneNumber(){
		emailSenderService.verifyPhoneNumber("0712345678");
	}

}

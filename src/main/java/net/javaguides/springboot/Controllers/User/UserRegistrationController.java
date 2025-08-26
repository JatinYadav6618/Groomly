package net.javaguides.springboot.Controllers.User;

import com.twilio.exception.ApiException;
import net.javaguides.springboot.model.RoleName;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.EmailSender.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.service.User.UserService;
import net.javaguides.springboot.web.dto.UserRegistrationDto;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user-registration")
public class UserRegistrationController {

	private final UserService userService;
	private final NotificationService emailSenderService;
	private final UserRepository userRepository;

	public UserRegistrationController(UserService userService, NotificationService emailSenderService, UserRepository userRepository) {
		super();
		this.userService = userService;
		this.emailSenderService = emailSenderService;
		this.userRepository = userRepository;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm(Model model) {
		String[] roleNames = Arrays.stream(RoleName.values())
				.map(Enum::name)
				.toArray(String[]::new);
		model.addAttribute("roleNames", roleNames);
		return "user-registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto, Model model, BindingResult result) {
		try {
			if(result.hasErrors()){
				return "user-registration";
			}
			if (userRepository.existsByEmail(registrationDto.getEmail())) {
				return "redirect:/user-registration?fail";
			}
			userService.save(registrationDto);
			emailSenderService.sendMessage(registrationDto.getPhoneNumber(), "Thank you for registering for our awesome app! " +
					"Hope you enjoy the Clay's experience");
			emailSenderService.sendMail(registrationDto.getEmail(), new String[]{},
					"Successful registration", "Thank you for registering for our awesome app! " +
							"Hope you enjoy the Clay's experience");
		} catch (ApiException e) {
			// Handle Twilio exception (e.g., unverified email or phone number)
			// You can add appropriate error handling logic or redirect to an error page
			model.addAttribute("infoMessage", "Phone number " +
					"unverified.");
			return "redirect:/user-registration?success&info"; //
			// Replace
			// with the actual name of your error page
		}
		return "redirect:/user-registration?success";
	}
}

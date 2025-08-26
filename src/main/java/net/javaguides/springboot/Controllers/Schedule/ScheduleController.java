package net.javaguides.springboot.Controllers.Schedule;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ScheduleRepository;
import net.javaguides.springboot.service.Schedule.ScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleService scheduleService, ScheduleRepository scheduleRepository) {
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/all")
    public String showScheduleList(Model model){
        List<Schedule> scheduleList= scheduleService.findAll();
        for(Schedule schedule : scheduleList){
            System.out.println(schedule);
        }
        System.out.println(scheduleList);
        model.addAttribute("scheduleList", scheduleList);
        return "schedules";
    }

    @GetMapping("/{id}")
    public String showScheduleListByBarber(@PathVariable("id")  Long id, Model model){
        List<Schedule> scheduleListByBarber=
                scheduleService.findByBarber(id);
        model.addAttribute("scheduleList",
                scheduleListByBarber);
        System.out.println(scheduleListByBarber);
        return "schedules";
    }
    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id,
                                 Model model) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new AppException("Invalid " +
                        "schedule" +
                        " Id:" + id));
        model.addAttribute("schedule", schedule);
        return "schedules";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             Schedule schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/schedules?success";
    }
}

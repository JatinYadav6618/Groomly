package net.javaguides.springboot.Controllers.BarberService;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.BarberServiceModel;
import net.javaguides.springboot.repository.BarberServiceRepositry;
import net.javaguides.springboot.service.BarberService.BarberService;
import net.javaguides.springboot.web.dto.BarberServiceDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/service")
public class BarberServiceController {
    private final BarberService barberService;
    private final BarberServiceRepositry barberServiceRepository;

    public BarberServiceController(BarberService barberService, BarberServiceRepositry barberServiceRepository) {
        this.barberService = barberService;
        this.barberServiceRepository = barberServiceRepository;
    }


    @ModelAttribute("barber_service")
    public BarberServiceDto serviceRegistrationDto() {
        return new BarberServiceDto();
    }

    @GetMapping("/add")
    public String showRegistrationForm() {
        return "service-registration";
    }

    @PostMapping("/add")
    public String addBarberService(@ModelAttribute("barber_service") BarberServiceDto barberServiceDto) {
        barberService.save(barberServiceDto);
        return "redirect:/service/add?success";
    }

    @GetMapping("/list")
    public String showBarberServiceList(Model model){
        List<BarberServiceModel> barberServiceList=barberService.listBarberServices();
        model.addAttribute("barberServiceList", barberServiceList);
        return "services";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id,
                               Model model){
        BarberServiceModel barberServiceModel=
                barberServiceRepository.findById(id).orElseThrow(()-> new AppException("Service with service id not found"+id));
        model.addAttribute("barber_service", barberServiceModel);
        return "update-service";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable("id") long id,
                                BarberServiceModel barberServiceModel){
        barberServiceRepository.save(barberServiceModel);
        return "redirect:/service/list?edit";
    }
    @GetMapping("/delete/{id}")
    public String deleteBookingService(@PathVariable("id") long id) {
        BarberServiceModel barberServiceModel = barberServiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid service Id:" + id));
        barberServiceRepository.delete(barberServiceModel);
        return "redirect:/service/list?delete";
    }
}

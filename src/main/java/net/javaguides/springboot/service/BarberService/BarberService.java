package net.javaguides.springboot.service.BarberService;

import net.javaguides.springboot.model.BarberServiceModel;
import net.javaguides.springboot.web.dto.BarberServiceDto;

import java.util.List;

public interface BarberService {
    List<BarberServiceModel> listBarberServices();
    BarberServiceModel save(BarberServiceDto barberServiceDto);
    BarberServiceModel findById(Long id);
}

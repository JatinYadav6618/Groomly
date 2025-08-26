package net.javaguides.springboot.service.BarberService;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.BarberServiceModel;
import net.javaguides.springboot.repository.BarberServiceRepositry;
import net.javaguides.springboot.web.dto.BarberServiceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberServiceImpl implements BarberService{

    private final BarberServiceRepositry barberServiceRepositry;

    public BarberServiceImpl(BarberServiceRepositry barberServiceRepositry){
        this.barberServiceRepositry=barberServiceRepositry;
    }
    @Override
    public List<BarberServiceModel> listBarberServices() {
        return barberServiceRepositry.findAll();
    }

    @Override
    public BarberServiceModel save(BarberServiceDto barberServiceDto) {
        BarberServiceModel barberServiceModel= new BarberServiceModel(
            barberServiceDto.getName(),barberServiceDto.getDescription()
        );
        System.out.println(barberServiceModel);
        return barberServiceRepositry.save(barberServiceModel);
    }

    @Override
    public BarberServiceModel findById(Long id) {
        return barberServiceRepositry.findById(id).orElseThrow(()->new AppException("Service not found"));
    }
}

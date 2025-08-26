package net.javaguides.springboot.service.Schedule;

import net.javaguides.springboot.AppException;
import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.ScheduleStatus;
import net.javaguides.springboot.repository.ScheduleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.ScheduleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository){
        super();
        this.scheduleRepository=scheduleRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(()-> new AppException("Schedule not found"));
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();

    }

    @Override
    public List<Schedule> findByBarber(Long id) {
        List<Schedule> scheduleListByBarber=
                scheduleRepository.findByBarberOrderByDateAscTimeAsc(userRepository.findById(id).orElseThrow(() -> new AppException("Barber not found")));
        for(Schedule schedule:scheduleListByBarber){
            System.out.println(schedule);
        }
        return scheduleListByBarber;
    }

    @Override
    public List<ScheduleDto> findScheduleWithDetails() {
        List<Schedule> schedules=scheduleRepository.findAll();
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for(Schedule schedule: schedules){
            ScheduleDto scheduleDto=
                    new ScheduleDto(schedule.getId(),
                            schedule.getBarber().getEmail(),
                            schedule.getDate().toString(),
                            schedule.getTime().toString(),
                            schedule.getStatus().toString());
                    scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public Schedule editBookingStatus(Long id, ScheduleDto scheduleDto){
        Schedule schedule=findById(id);
        schedule.setStatus(ScheduleStatus.valueOf(scheduleDto.getStatus()));
        return scheduleRepository.save(schedule);
    }
}

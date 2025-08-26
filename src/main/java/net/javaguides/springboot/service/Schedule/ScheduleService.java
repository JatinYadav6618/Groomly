package net.javaguides.springboot.service.Schedule;

import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.web.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {
    Schedule findById(Long id);
    List<Schedule> findAll();
    List<Schedule> findByBarber(Long id);
    List<ScheduleDto> findScheduleWithDetails();
    Schedule editBookingStatus(Long id, ScheduleDto scheduleDto);
}


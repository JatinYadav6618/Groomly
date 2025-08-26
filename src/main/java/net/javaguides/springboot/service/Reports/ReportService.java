package net.javaguides.springboot.service.Reports;

import net.javaguides.springboot.model.BarberServiceModel;
import net.javaguides.springboot.model.Booking;
import net.javaguides.springboot.model.Schedule;
import net.javaguides.springboot.model.User;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    String generateReport(String reportEntity) throws JRException
            , IOException;

}

package net.javaguides.springboot.service.Reports;

import net.javaguides.springboot.service.BarberService.BarberService;
import net.javaguides.springboot.service.Booking.BookingService;
import net.javaguides.springboot.service.Schedule.ScheduleService;
import net.javaguides.springboot.service.User.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService{

    private final UserService userService;
    private final BarberService barberService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;

    public ReportServiceImpl(UserService userService, BarberService barberService, ScheduleService scheduleService, BookingService bookingService) {
        this.userService = userService;
        this.barberService = barberService;
        this.scheduleService = scheduleService;
        this.bookingService = bookingService;
    }

    private JasperPrint getJasperPrint(List<?> dataList,
                                       String resourceLocation) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(resourceLocation);
        JasperReport jasperReport = JasperCompileManager
                .compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new
                JRBeanCollectionDataSource(dataList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Kris");

        return JasperFillManager
                .fillReport(jasperReport, parameters, dataSource);
    }
    private Path getUploadPath(JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("./generated-reports");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //generate the report and save it in the just created folder
        JasperExportManager.exportReportToPdfFile(jasperPrint, uploadPath+fileName);

        return uploadPath;
    }

    private String getPdfFileLink(String uploadPath, String entity){
        return uploadPath+"/"+entity+".pdf";
    }

    @Override
    public String generateReport(String entity) throws JRException
            , IOException {
        List<?> dataList = null;

        switch (entity.toLowerCase()) {
            case "user":
                dataList = userService.listAllUsers();
                break;

            case "schedule":
                dataList = scheduleService.findScheduleWithDetails();
                break;

            case "booking":
                dataList = bookingService.findBookingsWithDetails();
                break;

            case "service":
                dataList = barberService.listBarberServices();
                break;
        }
        //load the file and compile it
        String resourceLocation = "classpath:Reports/"+entity+
                "-reports" +
                ".jrxml";
        JasperPrint jasperPrint = getJasperPrint(dataList,
                resourceLocation);
        //create a folder to store the report
        String fileName = "/"+entity+".pdf";
        Path uploadPath = getUploadPath(jasperPrint, fileName);
        //create a private method that returns the link to the specific pdf file

        return getPdfFileLink(uploadPath.toString(), entity);
    }

}

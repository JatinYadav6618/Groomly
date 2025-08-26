package net.javaguides.springboot.Controllers.Reports;

import net.javaguides.springboot.service.Reports.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("reports")
public class ReportsController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/{entity}")
    public String generateReport(@PathVariable("entity") String reportEntity) throws JRException, IOException {
        String fileLink = reportService.generateReport(reportEntity);
        return "redirect:/"+fileLink;
    }
}

package net.javaguides.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class AppWebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String reportPath = uploadPath();

        registry.addResourceHandler("/generated-reports/**")
                .addResourceLocations("file:/"+reportPath+"/");
    }

    private String uploadPath(){
        Path uploadDirPath = Paths.get("./generated-reports");
        return uploadDirPath.toFile().getAbsolutePath();
    }
}
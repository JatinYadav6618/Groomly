package net.javaguides.springboot.web.dto;

public class BarberServiceDto {
    private String name;
    private String description;

    public BarberServiceDto(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public BarberServiceDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BarberServiceDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

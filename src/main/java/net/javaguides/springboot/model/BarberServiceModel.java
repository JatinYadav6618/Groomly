package net.javaguides.springboot.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="service")
public class BarberServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, columnDefinition = "TEXT", length = 500)
    private String description;

    @OneToMany(mappedBy = "service")
    private List<Booking> bookings;

    public BarberServiceModel() {
    }

    public BarberServiceModel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "BarberServiceModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

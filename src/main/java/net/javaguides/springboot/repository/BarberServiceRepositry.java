package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.BarberServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarberServiceRepositry extends JpaRepository<BarberServiceModel, Long> {
    Optional<BarberServiceModel> findById(Long Id);
    List<BarberServiceModel> findAll();
}

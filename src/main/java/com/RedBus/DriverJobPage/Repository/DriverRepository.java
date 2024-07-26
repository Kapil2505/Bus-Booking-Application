package com.RedBus.DriverJobPage.Repository;


import com.RedBus.DriverJobPage.Entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,String> {
}

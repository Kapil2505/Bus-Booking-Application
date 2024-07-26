package com.RedBus.DriverJobPage.Service;

import com.RedBus.DriverJobPage.Entity.Driver;
import com.RedBus.DriverJobPage.Repository.DriverRepository;
import com.RedBus.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService  {

    @Autowired
    private DriverRepository driverRepository;

    public Driver saveDriverData(Driver driver)
    {
        String id = UUID.randomUUID().toString();
        driver.setId(id);
        return driverRepository.save(driver);
    }

    public Driver getDetailsById(String id) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("details not found !"));
        return driver;
    }
}

package com.RedBus.DriverJobPage.Controller;

import com.RedBus.DriverJobPage.Entity.Driver;
import com.RedBus.DriverJobPage.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Driver")
public class DiverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/saveData")
    public ResponseEntity<Driver>saveDriverData(@RequestBody Driver driver)
    {
        return new ResponseEntity<>(driverService.saveDriverData(driver), HttpStatus.CREATED);
    }

    @GetMapping("/getDetails/{id}")
    public ResponseEntity<Driver>getDriverData(@PathVariable String id)
    {
         Driver driver = driverService.getDetailsById(id);
         return new ResponseEntity<>(driver,HttpStatus.OK);
    }
}

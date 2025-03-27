package com.RedBus.DriverJobPage.Controller;

import com.RedBus.DriverJobPage.Dto.DriverPageResponse;
import com.RedBus.DriverJobPage.Entity.Driver;
import com.RedBus.DriverJobPage.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    public ResponseEntity<DriverPageResponse>getAllDetails(@RequestParam(name="pageNo",defaultValue ="0",required = false )int pageNo, @RequestParam(name="pageSize",defaultValue = "5",required = false)int pageSize, @RequestParam(value = "sortBy",defaultValue = "id",required = false)String sortBy, @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir)
    {
        DriverPageResponse all = driverService.findAll(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
}

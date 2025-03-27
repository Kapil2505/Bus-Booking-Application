package com.RedBus.DriverJobPage.Service;

import com.RedBus.DriverJobPage.Dto.DriverPageResponse;
import com.RedBus.DriverJobPage.Entity.Driver;
import com.RedBus.DriverJobPage.Repository.DriverRepository;
import com.RedBus.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return driverRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("details not found !"));
    }

    public DriverPageResponse findAll(int pageNo , int pageSize , String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Driver> pageObject = driverRepository.findAll(pageable);
        List<Driver> content = pageObject.getContent();
        DriverPageResponse response = new DriverPageResponse();
        response.setDriversList(content);
        response.setPageNo(pageObject.getNumber());
        response.setPageSize(pageObject.getSize());
        response.setLastPage(pageObject.isLast());
        response.setTotalPages(pageObject.getTotalPages());
        return response;
    }
}

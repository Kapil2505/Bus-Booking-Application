package com.RedBus.Operator.Controller;


import com.RedBus.Operator.Service.OperatorService;
import com.RedBus.Operator.payload.BusOperatorDto;
import com.RedBus.Operator.payload.BusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/operator")
@Tag(name="company operator controller",description = "that api done CRUD operations on busOperator Entity")
public class OperatorController {

    private OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "put operation to add buses",
            description = "it added buses in database")
    @PostMapping("/addBus")
    public ResponseEntity<BusOperatorDto>saveBusDetails(@Valid @RequestBody BusOperatorDto busOperatorDto)
    {

        BusOperatorDto dto = operatorService.scheduleBus(busOperatorDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @Operation(summary = "get operation to get all buses",
            description = "it get all  buses and it is for app owner")
    @GetMapping("/allPageDetails")
    public BusResponse getAllBusDetails(@RequestParam(name="pageNo",defaultValue ="0",required = false )int pageNo,@RequestParam(name="pageSize",defaultValue = "5",required = false)int pageSize,@RequestParam(value = "sortBy",defaultValue = "busId",required = false)String sortBy,@RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir)
    {
        BusResponse response = operatorService.getAllBusDetails(pageNo,pageSize,sortBy,sortDir);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete operation on buses",
            description = "it delete buses using bus id")
    @DeleteMapping("/deleteBus/{busId}")
    public ResponseEntity<String>deleteBus(@PathVariable String busId)
    {
       operatorService.deleteBusDetails(busId);
       return new ResponseEntity<>("bus details is deleted !",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "put operation to update data of buses",
            description = "it updates buses data")
    @PutMapping("/updateBus/{busId}")
    public ResponseEntity<BusOperatorDto>updateBus(@PathVariable String busId , @RequestBody BusOperatorDto busDto)
    {
        BusOperatorDto dto = operatorService.updateBusDetails(busId,busDto);
        return  new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "get operation to get buses",
            description = "it get buses using busOperatorCompanyName .it is for bus operator company")
    @GetMapping("/searchBusesByCompanyName/{busOperatorCompanyName}")
    public List<BusOperatorDto>getBusesDetailsByCompanyName(@PathVariable String busOperatorCompanyName)
    {
        List<BusOperatorDto>buses=operatorService.getBusesByCompanyName(busOperatorCompanyName);
        return buses;
    }
}

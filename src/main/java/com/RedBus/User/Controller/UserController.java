package com.RedBus.User.Controller;

import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Service.OperatorService;
import com.RedBus.Operator.payload.BusOperatorDto;
import com.RedBus.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name="userController",description = "used for search buses using departure city ,arrival city and  departure date")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "get operation to search buses",
    description = "it searches buses using departure city ,arrival city and  departure date")
    @GetMapping("/Search")
    public List<BusOperatorDto>SearchBuses(@RequestParam String departureCity,
                                           @RequestParam String arrivalCity,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate)
    {
        List<BusOperatorDto>searchedBus = userService.searchBuses(departureCity,arrivalCity,departureDate);
        return searchedBus;
    }
}

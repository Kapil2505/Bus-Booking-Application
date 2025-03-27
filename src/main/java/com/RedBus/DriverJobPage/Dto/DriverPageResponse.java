package com.RedBus.DriverJobPage.Dto;

import com.RedBus.DriverJobPage.Entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverPageResponse {
    private List<Driver>driversList;
    private int pageNo;
    private int pageSize;
    private boolean isLastPage;
    private int totalPages;
}

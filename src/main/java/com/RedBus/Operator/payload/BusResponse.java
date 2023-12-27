package com.RedBus.Operator.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusResponse {
    private List<BusOperatorDto> busDtos;
    private long pageNo;
    private long pageSize;
    private boolean isLastPage;
    private long totalPages;
}

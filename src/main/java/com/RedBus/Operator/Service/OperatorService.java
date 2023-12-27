package com.RedBus.Operator.Service;

import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Entity.TicketPrice;
import com.RedBus.Operator.payload.BusOperatorDto;
import com.RedBus.Operator.payload.BusResponse;

import java.util.List;

public interface OperatorService {

    BusOperatorDto mapToDtoForBusOperator(BusOperator busOperator);

    BusOperator mapToEntityForBusOperator(BusOperatorDto busOperatorDto);


    BusOperatorDto scheduleBus(BusOperatorDto busOperatorDto);



    BusResponse getAllBusDetails(int pageNo, int pageSize, String sortBy, String sortDir);

    void deleteBusDetails(String busId);

    BusOperatorDto updateBusDetails(String busId, BusOperatorDto busDto);

    List<BusOperatorDto> getBusesByCompanyName(String busOperatorCompanyName);
}

package com.RedBus.User.Service;

import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Entity.TicketPrice;
import com.RedBus.Operator.payload.BusOperatorDto;

import java.util.Date;
import java.util.List;

public interface UserService {
    BusOperatorDto mapToDtoForBusOperator(BusOperator busOperator);

    BusOperator mapToEntityForBusOperator(BusOperatorDto busOperatorDto);



    List<BusOperatorDto> searchBuses(String departureCity, String arrivalCity, Date departureDate);
}

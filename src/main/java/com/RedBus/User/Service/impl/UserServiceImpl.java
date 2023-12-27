package com.RedBus.User.Service.impl;

import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Entity.TicketPrice;
import com.RedBus.Operator.Repository.BoardingPointsRepository;
import com.RedBus.Operator.Repository.BusOperatorRepository;
import com.RedBus.Operator.Repository.TicketPriceRepository;
import com.RedBus.Operator.payload.BusOperatorDto;
import com.RedBus.User.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private BusOperatorRepository busOperatorRepository;
    private TicketPriceRepository ticketPriceRepository;
    private BoardingPointsRepository boardingPointsRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(BusOperatorRepository busOperatorRepository, TicketPriceRepository ticketPriceRepository, BoardingPointsRepository boardingPointsRepository,ModelMapper modelMapper) {
        this.busOperatorRepository = busOperatorRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.boardingPointsRepository = boardingPointsRepository;
        this.modelMapper=modelMapper;
    }
    @Override
    public BusOperatorDto mapToDtoForBusOperator(BusOperator busOperator) {
        return modelMapper.map(busOperator,BusOperatorDto.class);
    }

    @Override
    public BusOperator mapToEntityForBusOperator(BusOperatorDto busOperatorDto) {
        return modelMapper.map(busOperatorDto,BusOperator.class);
    }



    @Override
    public List<BusOperatorDto> searchBuses(String departureCity, String arrivalCity,  Date departureDate) {
        List<BusOperator> searchResponse = busOperatorRepository.findByDepartureCityAndArrivalCityAndDepartureDate(departureCity, arrivalCity,  departureDate);
        return searchResponse.stream().map(x->mapToDtoForBusOperator(x)).collect(Collectors.toList());
    }

}

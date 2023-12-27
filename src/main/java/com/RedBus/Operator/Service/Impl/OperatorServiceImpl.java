package com.RedBus.Operator.Service.Impl;

import com.RedBus.Exception.ResourceNotFoundException;
import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Entity.TicketPrice;
import com.RedBus.Operator.Repository.BoardingPointsRepository;
import com.RedBus.Operator.Repository.BusOperatorRepository;
import com.RedBus.Operator.Repository.TicketPriceRepository;
import com.RedBus.Operator.Service.OperatorService;
import com.RedBus.Operator.payload.BusOperatorDto;
import com.RedBus.Operator.payload.BusResponse;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OperatorServiceImpl implements OperatorService {

    private BusOperatorRepository busOperatorRepository;
    private BoardingPointsRepository boardingPointsRepository;
   private TicketPriceRepository ticketPriceRepository;

   private ModelMapper modelMapper;

    public OperatorServiceImpl(BusOperatorRepository busOperatorRepository, BoardingPointsRepository boardingPointsRepository, TicketPriceRepository ticketPriceRepository, ModelMapper modelMapper) {
        this.busOperatorRepository = busOperatorRepository;
        this.boardingPointsRepository = boardingPointsRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BusOperatorDto mapToDtoForBusOperator(BusOperator busOperator) {
        Hibernate.initialize(busOperator.getTicketPrice());
        return modelMapper.map(busOperator,BusOperatorDto.class);
    }

    @Override
    public BusOperator mapToEntityForBusOperator(BusOperatorDto busOperatorDto) {
        return modelMapper.map(busOperatorDto,BusOperator.class);
    }



    @Override
    public BusOperatorDto scheduleBus(BusOperatorDto busOperatorDto) {
        BusOperator bus = mapToEntityForBusOperator(busOperatorDto);

        TicketPrice ticketPrice = new TicketPrice();
        String ticketId =UUID.randomUUID().toString();
        ticketPrice.setTicketId(ticketId);
        ticketPrice.setCode(busOperatorDto.getTicketPrice().getCode());
        ticketPrice.setTicketCost(busOperatorDto.getTicketPrice().getTicketCost());
        ticketPrice.setDiscountAmount(busOperatorDto.getTicketPrice().getDiscountAmount());
        ticketPrice.setTicketCostAfterDiscount((busOperatorDto.getTicketPrice().getTicketCost())-(busOperatorDto.getTicketPrice().getDiscountAmount()));

        bus.setTicketPrice(ticketPrice);

        String busId = UUID.randomUUID().toString();
        bus.setBusId(busId);

        return mapToDtoForBusOperator(busOperatorRepository.save(bus));
    }



    @Override
    public BusResponse getAllBusDetails(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BusOperator> pageObject = busOperatorRepository.findAll(pageable);
        List<BusOperator> bus = pageObject.getContent();
        List<BusOperatorDto> busDtos = bus.stream().map(x -> mapToDtoForBusOperator(x)).collect(Collectors.toList());
        BusResponse response = new BusResponse();
        response.setBusDtos(busDtos);
        response.setPageNo(pageObject.getNumber());
        response.setPageSize(pageObject.getSize());
        response.setLastPage(pageObject.isLast());
        response.setTotalPages(pageObject.getTotalPages());

        return response;
    }

    @Override
    public void deleteBusDetails(String busId) {
         busOperatorRepository.findById(busId).orElseThrow(()-> new ResourceNotFoundException("Bus details not found"));
         busOperatorRepository.deleteById(busId);

    }

    @Override
    public BusOperatorDto updateBusDetails(String busId, BusOperatorDto busDto) {

        BusOperator bus = busOperatorRepository.findById(busId).orElseThrow(()-> new ResourceNotFoundException("Bus details not found"));
        BusOperator bus1 = mapToEntityForBusOperator(busDto);
        bus1.setBusId(bus.getBusId());
        return mapToDtoForBusOperator(busOperatorRepository.save(bus1));
    }

    @Override
    public List<BusOperatorDto> getBusesByCompanyName(String busOperatorCompanyName) {
        List<BusOperator> buses = busOperatorRepository.findByBusOperatorCompanyName(busOperatorCompanyName);

        return buses.stream().map(x->mapToDtoForBusOperator(x)).collect(Collectors.toList());
    }
}

package com.RedBus.Operator.Repository;

import com.RedBus.Operator.Entity.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BusOperatorRepository extends JpaRepository<BusOperator,String> {
//  @Query("SELECT b FROM BusOperator b " +
//            "WHERE b.departureCity = :departureCity " +
//            "AND b.arrivalCity = :arrivalCity " +
//            "AND DATE(b.departureDate) = :formattedDate")
//    List<BusOperator> findBusOperatorsByCitiesAndDate(
//            @Param("departureCity") String departureCity,
//            @Param("arrivalCity") String arrivalCity,
//            @Param("formattedDate") Date formattedDate
//    );

List<BusOperator>findByDepartureCityAndArrivalCityAndDepartureDate(String departureCity,String arrivalCity,Date departureDate);


    List<BusOperator>findByBusOperatorCompanyName(String busOperatorCompanyName);
}

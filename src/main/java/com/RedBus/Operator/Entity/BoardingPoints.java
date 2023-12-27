package com.RedBus.Operator.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="boarding_points")
public class BoardingPoints {
    @Id
    @Column(name="boarding_point_id")
    private String boardingPointId;
    @Column(name="boarding_location")
    private String boardingLocation;
    @Column(name="boarding_time")
    private LocalTime boardingTime;

    /*@ManyToOne
    @JoinColumn(name="bus_id")
    private BusOperator busOperator;*/
}

package com.RedBus.Operator.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardingPointsDto {
    private String boardingPointId;
    private String boardingLocation;
    private LocalTime boardingTime;

}

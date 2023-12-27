package com.RedBus.Operator.Repository;

import com.RedBus.Operator.Entity.BoardingPoints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardingPointsRepository extends JpaRepository<BoardingPoints,String> {
}

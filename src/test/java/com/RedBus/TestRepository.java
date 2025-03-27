package com.RedBus;

import com.RedBus.Operator.Entity.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<BusOperator,String> {
}

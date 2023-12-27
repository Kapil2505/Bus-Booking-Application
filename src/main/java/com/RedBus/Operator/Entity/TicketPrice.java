package com.RedBus.Operator.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ticket_prices")
public class TicketPrice {

    @Id
    @Column(name="ticket_id")
    private String ticketId;

    @Column(name="ticket_cost")
    private double ticketCost;
    @Column(name="code")
    private String  code;
    @Column(name="discount_amount")
    private double discountAmount;
    @Column(name="ticket_cost_after_discount")
    private double ticketCostAfterDiscount;
    @OneToOne(mappedBy = "ticketPrice",cascade = CascadeType.ALL)
    @JsonIgnore
    private BusOperator busOperator;

}

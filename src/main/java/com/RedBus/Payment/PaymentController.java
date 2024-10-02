package com.RedBus.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/Intent/{ticketCost}")
    public ResponseEntity<String>payment(@PathVariable double ticketCost)
    {
       String key =  paymentService.createPaymentIntent((int)ticketCost);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }
}

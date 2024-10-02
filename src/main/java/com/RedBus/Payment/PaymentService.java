package com.RedBus.Payment;

import com.RedBus.Exception.ResourceNotFoundException;
import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Repository.BusOperatorRepository;
import com.RedBus.User.Entity.Booking;
import com.RedBus.User.Repository.BookingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Service
public class PaymentService {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusOperatorRepository busOperatorRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public String createPaymentIntent(Integer amount) {
        Stripe.apiKey = stripeApiKey;
        try {

            PaymentIntent intent = PaymentIntent.create(new PaymentIntentCreateParams.Builder().setCurrency("usd").setAmount((long) amount * 100).build());
            logger.info("payment done");
            return generateResponse(intent.getClientSecret());

        } catch (StripeException e) {
            return generateResponse("Error creating PaymentIntent: " + e.getMessage());
        }

    }

    private String generateResponse(String clientSecret) {
        return clientSecret;
    }

}


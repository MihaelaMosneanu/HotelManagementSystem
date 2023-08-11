package com.hotelsystem.management.repository;

import com.hotelsystem.management.model.Payment;

import java.util.List;

public interface PaymentRepository {
    void addPayment(Payment payment) ;

    List<Payment> findAll() ;

    void updatePayment(int id, Payment payment);

    void deletePayment(int id) ;

    Payment findPaymentById(int id) ;
}

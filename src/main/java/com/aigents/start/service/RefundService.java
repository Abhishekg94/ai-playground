package com.aigents.start.service;

import org.springframework.stereotype.Service;

import com.aigents.start.entity.RefundPolicy;
import com.aigents.start.repository.RefundPolicyRepository;

@Service
public class RefundService {

    private RefundPolicyRepository refundPolicyRepository;

    RefundService(RefundPolicyRepository refundPolicyRepository) {
        this.refundPolicyRepository = refundPolicyRepository;
    }

    public RefundPolicy checkEligibility(String orderStatus) {
        System.out.print("\nREFUND SERVICE -> checking status " + orderStatus);
        return refundPolicyRepository.findByOrderStatus(orderStatus)
                .orElseThrow(() -> new RuntimeException("refund plociy not found"));
    }
}

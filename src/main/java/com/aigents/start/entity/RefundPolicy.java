package com.aigents.start.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "refund_policy")
public class RefundPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_status", length = 30, nullable = false, unique = true)
    private String orderStatus;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "eligible", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private Boolean eligible = false;

    public RefundPolicy() {
    }

    public RefundPolicy(String orderStatus, String message, Boolean eligible) {
        this.orderStatus = orderStatus;
        this.message = message;
        this.eligible = eligible != null ? eligible : false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }
}

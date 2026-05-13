package com.bridgelabz.purchaseservice.entity;

public enum OrderStatus {
    CREATED,
    DRAFT,
    PENDING,
    APPROVED,
    SHIPPED,
    DELIVERED,
    PARTIALLY_RECEIVED,
    FULLY_RECEIVED,
    CANCELLED,
    PENDING_APPROVAL,
    RECEIVED // Keeping for backward compatibility if used in code
}

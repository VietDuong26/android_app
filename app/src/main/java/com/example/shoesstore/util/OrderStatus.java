package com.example.shoesstore.util;

public enum OrderStatus {
    ORDERED(0),
    SHIPPING(1),
    SHIPPED(2);

    private int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getStatusName(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                switch (status) {
                    case ORDERED:
                        return "Đã đặt";
                    case SHIPPING:
                        return "Đang giao";
                    case SHIPPED:
                        return "Đã giao";
                }
            }
        }
        return "Không xác định";
    }
}

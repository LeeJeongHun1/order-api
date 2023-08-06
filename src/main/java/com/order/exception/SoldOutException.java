package com.order.exception;

/**
 * 주문 수량이 재고 수량보다 많을 경우 발생되는 예외
 */
public class SoldOutException extends RuntimeException {

    private static final String MESSAGE = "The product is ordered is out of stock";
    public SoldOutException() {
        super(MESSAGE);
    }
    public SoldOutException(Throwable cause) {
        super(MESSAGE, cause);
    }

}

package com.zenith.payment_gateway.exception;

public class EmailAlreadyExistsException extends RuntimeException {
 public EmailAlreadyExistsException(String message){
     super(message);
 }
}

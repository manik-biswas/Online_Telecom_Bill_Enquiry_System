//package com.otbs.exception;
//
//public class InvalidEntityException extends RuntimeException {
//    public InvalidEntityException(String message) {
//        super(message);
//    }
//}



package com.otbs.exception;

public class InvalidEntityException extends Exception {
    public InvalidEntityException(String message) {
        super(message);
    }
}
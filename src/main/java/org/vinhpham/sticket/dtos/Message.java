package org.vinhpham.sticket.dtos;

public record Message(String message) {
    @Override
    public String toString() {
        return "{\"message\":\"" + message + "\"}";
    }
}

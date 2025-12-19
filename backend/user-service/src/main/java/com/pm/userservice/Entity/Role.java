package com.pm.userservice.Entity;


public enum Role {
    ADMIN("ADMIN"),
    WORKER("WORKER"),
    COMMITTEE("COMMITTEE");
    private String value;
    Role(String value) {}
}

package com.pm.userservice.Entity;

import lombok.Getter;

import java.security.Permission;

@Getter
public enum Premission {

    // pregrammes management
    MANAGE_PROGRAMMES("MANAGE_PROGRAMMES" , "create , update and delete committee programmes"),
    VIEW_PROGRAMMES("VIEW_PROGRAMMES" , "juste view the programmes"),

    // demands management
    VIEW_ALL_DEMANDS("VIEW_ALL_DEMANDS", "View all employee demands"),
    REVIEW_DEMANDS("REVIEW_DEMANDS", "Review and validate demands"),
    APPROVE_DEMANDS("APPROVE_DEMANDS", "Approve or reject demands"),
    // meeting management
    SCHEDULE_MEETINGS("SCHEDULE_MEETINGS", "Schedule committee meetings"),
    ATTEND_MEETINGS("ATTEND_MEETINGS", "Attend committee meetings"),

    // treasurer management
    VALIDATE_PAYMENTS("VALIDATE_PAYMENTS", "Validate payment requests"),
    MANAGE_BUDGET("MANAGE_BUDGET", "Manage committee budget");

    private final String  code;
    private final String  description;
    Premission(String code , String description) {
        this.code = code;
        this.description = description;
    }

}

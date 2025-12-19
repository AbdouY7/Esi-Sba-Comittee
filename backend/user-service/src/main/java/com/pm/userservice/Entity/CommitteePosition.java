package com.pm.userservice.Entity;



import lombok.Getter;

import java.security.Permission;

@Getter
public enum CommitteePosition {


    PRESIDENT("PRESIDENT", "Committee President", new Premission[]{
        Premission.MANAGE_PROGRAMMES ,
            Premission.APPROVE_DEMANDS ,
            Premission.SCHEDULE_MEETINGS ,
            Premission.VIEW_ALL_DEMANDS,
            Premission.VIEW_PROGRAMMES
    }),
    VICE_PRESIDENT("VICE_PRESIDENT", "committee vice president" , new Premission[]{
            Premission.MANAGE_PROGRAMMES ,
            Premission.APPROVE_DEMANDS ,
            Premission.SCHEDULE_MEETINGS ,
            Premission.VIEW_ALL_DEMANDS,
            Premission.VIEW_PROGRAMMES
    }) ,
    TREASURER("TREASURER", "committee treasurer" , new Premission[]{
            Premission.VIEW_PROGRAMMES ,
            Premission.MANAGE_BUDGET ,
            Premission.VALIDATE_PAYMENTS ,
    }) ,
    MEMBER("MEMBER", "committee normal member" , new Premission[]{
            Premission.VIEW_ALL_DEMANDS ,
            Premission.VIEW_PROGRAMMES,
            Premission.REVIEW_DEMANDS ,
            Premission.ATTEND_MEETINGS
    });

    @Getter
    private final String code ;
    private final  String description ;
    private final Premission[] premissions ;
    CommitteePosition(String code, String description, Premission[] premissions) {
        this.code = code;
        this.description = description;
        this.premissions = premissions;
    }

    public boolean hasPremission(Premission premission) {
        for (Premission p : this.premissions) {
            if (p == premission)
                return true;
        }
        return   false;

    }


}

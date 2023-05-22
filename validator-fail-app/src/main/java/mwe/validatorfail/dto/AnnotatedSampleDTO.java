package mwe.validatorfail.dto;


import mwe.validatorfail.validation.AnyUuid;

import javax.validation.constraints.NotNull;

public class AnnotatedSampleDTO {
    @NotNull
    @AnyUuid
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public AnnotatedSampleDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}

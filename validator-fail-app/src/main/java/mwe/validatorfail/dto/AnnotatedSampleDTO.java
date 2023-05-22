package mwe.validatorfail.dto;


import jakarta.validation.constraints.NotNull;
import mwe.validatorfail.validation.AnyUuid;


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

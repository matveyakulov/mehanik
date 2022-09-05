package ru.neirodev.mehanik.enums;

public enum PartType {

    PASSENGER("2"),
    COMMERCIAL("16"),
    MOTO("777"),
    ENGINE("14"),
    AXLE("19");

    PartType(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }
}

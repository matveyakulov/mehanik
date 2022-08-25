package ru.neirodev.mehanik.enums;

public enum CarType {

    PASSENGER("passenger", "2"),
    COMMERCIAL("commercial", "16"),
    MOTO("moto", "777");

    CarType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private final String name;

    private final String code;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}

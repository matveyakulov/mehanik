package ru.neirodev.mehanik.enums;

public enum CarType {

    PASSENGER("passenger"),
    COMMERCIAL("commercial"),
    MOTO("moto"),
    AXLE("axle");

    CarType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}

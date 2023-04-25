package com.example.aviasales.util.enums;

public enum SortingAlgorithm {
    CHEAP_FIRST("min_price"),
    DEPARTURE_TIME("departure_time"),
    ARRIVAL_TIME("arrival_time");

    public final String sortingField;

    private SortingAlgorithm(String sortingField) {
        this.sortingField = sortingField;
    }
}

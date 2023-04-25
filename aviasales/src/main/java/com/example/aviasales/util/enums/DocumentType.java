package com.example.aviasales.util.enums;

public enum DocumentType {
    PASSPORT("Паспорт"),
    VISA("Загран.паспорт"),
    MILITARY_RECORD("Военный билет"),
    BIRTH_CERTIFICATE("Свидетельство о рождении");

    public final String documentName;

    private DocumentType(String documentName) {
        this.documentName = documentName;
    }
}

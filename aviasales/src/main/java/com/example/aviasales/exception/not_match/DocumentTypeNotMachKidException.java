package com.example.aviasales.exception.not_match;


public class DocumentTypeNotMachKidException extends ResourceNotMatchException {
    public DocumentTypeNotMachKidException(String documentType, Boolean isKid) {
        super("document-type [" + documentType + "]",
                "is-kid [" + isKid.toString() + "]");
    }
}

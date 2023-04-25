package com.example.aviasales.exception.not_match;

public class AirlinesNotMatchesException extends ResourceNotMatchException {
    public AirlinesNotMatchesException(String entityName1, String entityName2, Long airlineId1, Long airlineId2) {
        super(entityName1 + " airline-id [" + airlineId1.toString() + "]",
                entityName2 + " airline-id [" + airlineId2.toString() + "]");
    }
}

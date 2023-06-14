package com.example.aviasales.dto;

import java.io.Serializable;

public record ReSendMailsResult(
        int jobs,
        int successfullyReSent
) implements Serializable {

}

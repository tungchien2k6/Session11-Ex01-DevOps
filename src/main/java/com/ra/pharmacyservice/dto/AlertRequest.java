package com.ra.pharmacyservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRequest {
    private String topicListen;
    private String type;
    private String message;
}

package com.adnancrnovrsanin.ping.auth.DTOs;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitialRequest {
    private String phoneNumber;
}

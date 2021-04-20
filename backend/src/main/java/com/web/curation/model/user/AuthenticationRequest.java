package com.web.curation.model.user;


import lombok.*;

@NoArgsConstructor
@Data
@ToString
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    
    String email;
    String password;
    String name;
    
}

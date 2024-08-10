package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {
    private String name;
    private Role role;
    private String email;
}

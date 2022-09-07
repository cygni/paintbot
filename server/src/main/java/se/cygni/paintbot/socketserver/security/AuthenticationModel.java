package se.cygni.paintbot.socketserver.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationModel {
    private String username;
    private String password;
}

package se.cygni.paintbot.socketserver.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModel {
    private String username;
    private String name;
    private String role;
}

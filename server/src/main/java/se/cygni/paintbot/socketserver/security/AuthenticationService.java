package se.cygni.paintbot.socketserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public String authenticate(String login, String password) throws UsernameNotFoundException, CredentialException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        boolean passwordMatches = passwordEncoder.matches(password, userDetails.getPassword());
        if (passwordMatches) {
            return jwtTokenUtil.generateToken(userDetails);
        } else {
            throw new CredentialException("Username and Password do not match");
        }
    }
}

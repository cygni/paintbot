package se.cygni.paintbot.socketserver.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.cygni.paintbot.socketserver.SocketServerApplication;
import se.cygni.paintbot.socketserver.security.AuthenticationController;
import se.cygni.paintbot.socketserver.security.AuthenticationModel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {AuthenticationController.class})
@ContextConfiguration(classes = {SocketServerApplication.class})
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    private final static String API = "/api/v1";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private final AuthenticationModel trams = new AuthenticationModel("trams", "p455w0rD");
    private final AuthenticationModel cygniUser = new AuthenticationModel("cygni", "p455w0rD");

    @Test
    @WithAnonymousUser
    public void authenticateWithoutRequestBody() throws Exception {
        mockMvc.perform(post(API + "/authenticate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void authenticateWithAccountNotFound() throws Exception {
        mockMvc.perform(post(API + "/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trams)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void authenticateWithInvaliPassword() throws Exception {
        mockMvc.perform(post(API + "/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cygniUser)))
                .andExpect(status().isUnauthorized());
    }

}

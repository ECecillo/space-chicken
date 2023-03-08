package fr.univlyon1.m1if.m1if13.users.functionnal;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersOperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testLoginUserJSON() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")
                        .content("{"
                                + "\"login\":\"Susan\","
                                + "\"password\":\"susanPassword\""
                                + "}"))
                .andExpect(status().isNoContent()); // 204 HTTP code for JSON request format.
    }

    @Test
    @Order(6)
    public void testLoginUserXML() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "<password>susanPassword</password>"
                                + "</user>"))
                .andExpect(status().isNoContent()); // 204 HTTP code for XML request format.
    }

    @Test
    @Order(2)
    public void testAuthenticateUserJSON() throws Exception {
        // LOGIN
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")
                        .content("{"
                                + "\"login\":\"Susan\","
                                + "\"password\":\"susanPassword\""
                                + "}"))
                .andExpect(status().isNoContent()) // 204 HTTP code
                .andReturn();
        String token = result.getResponse().getHeader("Authorization");
        String cleanToken = token.replace("Bearer ", ""); //get the token
        // TEST AUTHENTICATE
        mockMvc.perform(get("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("jwt", cleanToken)
                        .param("Origin", "http://localhost:8080"))
                .andExpect(status().isNoContent()); // 204 HTTP code
    }

    @Test
    @Order(7)
    public void testAuthenticateUserXML() throws Exception {
        // LOGIN
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "<password>susanPassword</password>"
                                + "</user>"))
                .andExpect(status().isNoContent()) // 204 HTTP code
                .andReturn();
        String token = result.getResponse().getHeader("Authorization");
        String cleanToken = token.replace("Bearer ", ""); //get the token
        // TEST AUTHENTICATE
        mockMvc.perform(get("/authenticate")
                        .contentType(MediaType.APPLICATION_XML)
                        .param("jwt", cleanToken)
                        .param("Origin", "http://localhost:8080"))
                .andExpect(status().isNoContent()); // 204 HTTP code
    }

    @Test
    @Order(3)
    public void testLogoutUserJSON() throws Exception {
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")
                        .content("{"
                                + "\"login\":\"Susan\""
                                + "}"))
                .andExpect(status().isNoContent()); // 204 HTTP code user correctly logged out.
    }

    @Test
    @Order(8)
    public void testLogoutUserXML() throws Exception {
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "</user>"))
                .andExpect(status().isNoContent()); // 204 HTTP code user correctly logged out.
    }

    @Test
    @Order(4)
    public void testLoginWrongPasswordJSON() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")
                        .content("{"
                                + "\"login\":\"Susan\","
                                + "\"password\":\"wrongPassword\""
                                + "}"))
                .andExpect(status().isUnauthorized()); // 401 HTTP code
    }

    @Test
    @Order(9)
    public void testLoginWrongPasswordXML() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "<password>wrongPassword</password>"
                                + "</user>"))
                .andExpect(status().isUnauthorized()); // 401 HTTP code
    }

    @Test
    @Order(5)
    public void testLoginWrongUserJSON() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")
                        .content("{"
                                + "\"login\":\"wrongUser\","
                                + "\"password\":\"wrongPassword\""
                                + "}"))
                .andExpect(status().isNotFound()); // 404 HTTP code
    }

    @Test
    @Order(10)
    public void testLoginWrongUserXML() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")
                        .content("<user>"
                                + "<login>wrongUser</login>"
                                + "<password>wrongPassword</password>"
                                + "</user>"))
                .andExpect(status().isNotFound()); // 404 HTTP code
    }


}

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

/**
 * Test class for the UsersOperationsController class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersOperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the login method with JSON content type in body.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the login method with XML content type in body.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the authenticate method with JSON content type in body and right jwt.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the authenticate method with XML content type in body and right jwt.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the logoutUser method with JSON content type in body.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the logoutUser method with XML content type in body.
     * Should return a 204 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the login method with JSON content type in body and wrong password.
     * Should return a 401 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the login method with XML content type in body and wrong password.
     * Should return a 401 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the login method with JSON content type in body and wrong user.
     * Should return a 404 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test the login method with XML content type in body and wrong user.
     * Should return a 404 HTTP code.
     * @throws Exception if an error occurs.
     */
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

    /**
     * Test CORS for login JSON request format.
     * Should return 204 HTTP code for correct Origin.
     * Should return 401 HTTP code for wrong Origin.
     * @throws Exception
     */
    @Test
    @Order(11)
    public void testLoginCorsJSON() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")// correct origin.
                        .content("{"
                                + "\"login\":\"Susan\","
                                + "\"password\":\"susanPassword\""
                                + "}"))
                .andExpect(status().isNoContent()); // 204 HTTP code.
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://wrong:8080")
                        .content("{"
                                + "\"login\":\"Susan\","
                                + "\"password\":\"susanPassword\""
                                + "}"))
                .andExpect(status().isForbidden()); // 403 HTTP code.
    }

    /**
     * Test CORS for login XML request format.
     * Should return 204 HTTP code for correct Origin.
     * Should return 401 HTTP code for wrong Origin.
     * @throws Exception
     */
    @Test
    @Order(13)
    public void testLoginCorsXML() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")// correct origin.
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "<password>susanPassword</password>"
                                + "</user>"))
                .andExpect(status().isNoContent()); // 204 HTTP code.
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://wrong:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "<password>susanPassword</password>"
                                + "</user>"))
                .andExpect(status().isForbidden()); // 403 HTTP code.
    }

    /**
     * Test CORS for logout JSON request format.
     * Should return 204 HTTP code for correct Origin.
     * Should return 401 HTTP code for wrong Origin.
     * @throws Exception
     */
    @Test
    @Order(12)
    public void testLogoutCorsJSON() throws Exception {
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:8080")// correct origin.
                        .content("{"
                                + "\"login\":\"Susan\""
                                + "}"))
                .andExpect(status().isNoContent()); // 204 HTTP code.
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://wrong:8080")
                        .content("{"
                                + "\"login\":\"Susan\""
                                + "}"))
                .andExpect(status().isForbidden()); // 403 HTTP code.
    }

    /**
     * Test CORS for logout XML request format.
     * Should return 204 HTTP code for correct Origin.
     * Should return 401 HTTP code for wrong Origin.
     * @throws Exception
     */
    @Test
    @Order(14)
    public void testLogoutCorsXML() throws Exception {
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://localhost:8080")// correct origin.
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "</user>"))
                .andExpect(status().isNoContent()); // 204 HTTP code.
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_XML)
                        .header("Origin", "http://wrong:8080")
                        .content("<user>"
                                + "<login>Susan</login>"
                                + "</user>"))
                .andExpect(status().isForbidden()); // 403 HTTP code.
    }
}

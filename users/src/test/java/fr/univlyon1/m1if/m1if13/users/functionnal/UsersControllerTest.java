package fr.univlyon1.m1if.m1if13.users.functionnal;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the UsersController class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests the getAllUsers method in UsersController with JSON content response.
     * Should return 200 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(1)
    public void testGetAllUsersJSON() throws Exception {
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 HTTP code.
                .andExpect(content().string(containsString(
                        "[\"ECecillo\","
                                + "\"Susan\","
                                + "\"Elfenwaar\","
                                + "\"Melp\","
                                + "\"John\","
                                + "\"admin\"]"))
                );
    }

    /**
     * Tests the getAllUsers method in UsersController with XML content response.
     * Should return 200 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(1)
    public void testGetAllUsersXML() throws Exception {
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk()) // 200 HTTP code.
                .andExpect(content().string(containsString(
                        "<Set>"
                                + "<item>ECecillo</item>"
                                + "<item>Susan</item>"
                                + "<item>Elfenwaar</item>"
                                + "<item>Melp</item>"
                                + "<item>John</item>"
                                + "<item>admin</item>"
                                + "</Set>"))
                );
    }

    /**
     * Tests the createUser method in UsersController with JSON content response.
     * Should return 201 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(2)
    public void testCreateUserJSON() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"login\":\"newUserJSON\","
                                + "\"password\":\"password\","
                                + "\"species\":\"CHICKEN\","
                                + "\"image\":\"imageUrl\""
                                + "}"
                        )) // User created.
                .andExpect(status().isCreated()); // 201 HTTP code user is created.
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(
                        "[\"ECecillo\","
                        + "\"newUserJSON\","
                        + "\"Susan\","
                        + "\"Elfenwaar\","
                        + "\"Melp\","
                        + "\"John\","
                        + "\"admin\"]"))
                ); // Check user correctly added.
    }

    /**
     * Tests the createUser method in UsersController with XML content response.
     * Should return 201 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(2)
    public void testCreateUserXML() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_XML)
                        .content("<user>"
                                + "<login>newUserXML</login>"
                                + "<password>password</password>"
                                + "<species>CHICKEN</species>"
                                + "<image>imageUrl</image>"
                                + "</user>"
                        )) // User created.
                .andExpect(status().isCreated()); // 201 HTTP code user is created.
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString(
                        "<Set>"
                                + "<item>newUserXML</item>"
                                + "<item>ECecillo</item>"
                                + "<item>newUserJSON</item>"
                                + "<item>Susan</item>"
                                + "<item>Elfenwaar</item>"
                                + "<item>Melp</item>"
                                + "<item>John</item>"
                                + "<item>admin</item>"
                                + "</Set>"))
                ); // Check user correctly added.
    }

    /**
     * Tests the getUser method in UsersController with JSON content response.
     *  Should return 200 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(3)
    public void testGetUserJSON() throws Exception {
        mockMvc.perform(get("/users/{name}", "newUserJSON")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 HTTP code
                .andExpect(content().string(containsString(
                        "{\"login\":\"newUserJSON\","
                        + "\"species\":\"CHICKEN\","
                        + "\"connected\":false,"
                        + "\"image\":\"imageUrl\"}"))
                );
    }

    /**
     * Tests the getUser method in UsersController with XML content response.
     * Should return 200 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(3)
    public void testGetUserXML() throws Exception {
        mockMvc.perform(get("/users/{name}", "newUserXML")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk()) // 200 HTTP code
                .andExpect(content().string(containsString(
                        "<User>"
                        + "<login>newUserXML</login>"
                        + "<species>CHICKEN</species>"
                        + "<connected>false</connected>"
                        + "<image>imageUrl</image>"
                        + "</User>"))
                );
    }

    /**
     * Tests the getUser method in UsersController when user in param do not exist.
     * Should return 404 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/users/{name}", "notExist"))
                .andExpect(status().isNotFound()); // 404 HTTP code
    }

    /**
     * Tests the deleteUser method in UsersController with JSON content response.
     * Should return 204 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(4)
    public void testDeleteUserJSON() throws Exception {
        mockMvc.perform(delete("/users/{name}", "newUserJSON")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // 204 HTTP code.
                .andExpect(content().string(containsString("User deleted successfully")));
        mockMvc.perform(get("/users"))
                .andExpect(content().string(
                        containsString("[\"newUserXML\","
                                + "\"ECecillo\","
                                + "\"Susan\","
                                + "\"Elfenwaar\","
                                + "\"Melp\","
                                + "\"John\","
                                + "\"admin\"]"))
                ); // User deleted.
    }

    /**
     * Tests the deleteUser method in UsersController with XML content response.
     * Should return 204 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    @Order(4)
    public void testDeleteUserXML() throws Exception {
        mockMvc.perform(delete("/users/{name}", "newUserXML")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNoContent()) // 204 HTTP code.
                .andExpect(content().string(containsString("User deleted successfully")));
        mockMvc.perform(get("/users"))
                .andExpect(content().string(containsString("[\"ECecillo\","
                        + "\"Susan\","
                        + "\"Elfenwaar\","
                        + "\"Melp\","
                        + "\"John\","
                        + "\"admin\"]"))
                ); // User deleted.
    }

    /**
     * Tests the deleteUser method in UsersController when user in param do not exist.
     * Should return 404 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/{name}", "notExist"))
                .andExpect(status().isNotFound()); // 404 HTTP code user not found.
    }

    /**
     * Tests the createUser method in UsersController when user already exist.
     * Should return 409 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testCreateUserAlreadyExist() throws Exception {
        String json = "{"
                + "\"login\":\"Susan\","
                + "\"password\":\"password\","
                + "\"species\":\"CHICKEN\","
                + "\"image\":null"
                + "}";
        String xml = "<user>"
                + "<login>Susan</login>"
                + "<password>password</password>"
                + "<species>CHICKEN</species>"
                + "<image>imageUrl</image>"
                + "</user>";
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict()); // 409 HTTP code failed created user JSON format.
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_XML).content(xml))
                .andExpect(status().isConflict()); // 409 HTTP code failed created user XML format.
    }

    /**
     * Tests the updateUserPassword method in UsersController.
     * Should return 204 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testUpdateUserPassword() throws Exception {
        mockMvc.perform(put("/users/{name}", "ECecillo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"
                        + "\"password\":\"newPassword\""
                        + "}"))
                .andExpect(status().isNoContent()); // 204 HTTP code, password updated correctly JSON format.
        mockMvc.perform(put("/users/{name}", "ECecillo")
                        .contentType(MediaType.APPLICATION_XML)
                        .content("<user>"
                                + "<password>newPassword</password>"
                                + "</user>"))
                .andExpect(status().isNoContent()); // 204 HTTP code, password updated correctly XML format.
    }

    /**
     * Tests the updateUserPassword method in UsersController when user in param do not exist.
     * Should return 404 HTTP code.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testUpdateUserPasswordNotFound() throws Exception {
        mockMvc.perform(put("/users/{name}", "notExist")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"
                        + "\"password\":\"newPassword\""
                        + "}"))
                .andExpect(status().isNotFound()); // 404 HTTP code, user not found in JSON format.
        mockMvc.perform(put("/users/{name}", "notExist")
                        .contentType(MediaType.APPLICATION_XML)
                        .content("<user>"
                                + "<password>newPassword</password>"
                                + "</user>"))
                .andExpect(status().isNotFound()); // 404 HTTP code, user not found in XML format.
    }

    /**
     * Test getUser CORS.
     * Should return 200 HTTP code without Origin.
     * Should return 200 HTTP code when Origin is correct.
     * Should return 403 HTTP code when Origin is incorrect.
     * @throws Exception when an error occurs.
     */
    @Test
    public void testGetUserCors() throws Exception {
        mockMvc.perform(get("/users/{name}", "Susan")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 200 HTTP without Origin.
        mockMvc.perform(get("/users/{name}", "Susan")
                        .accept(MediaType.APPLICATION_JSON)
                .header("Origin", "http://localhost:8080"))
                .andExpect(status().isOk()); // 200 HTTP code when Origin is correct.
        mockMvc.perform(get("/users/{name}", "Susan")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://wrong:8080"))
                .andExpect(status().isForbidden()); // 403 HTTP code when Origin is incorrect.
    }
}

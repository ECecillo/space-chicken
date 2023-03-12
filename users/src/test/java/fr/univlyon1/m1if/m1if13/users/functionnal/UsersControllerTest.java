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

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testGetAllUsersJSON() throws Exception {
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 HTTP code.
                .andExpect(content().string(containsString(
                        "[\"ECecillo\","
                                + "\"Susan\","
                                + "\"John\"]"))
                );
    }

    @Test
    @Order(1)
    public void testGetAllUsersXML() throws Exception {
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk()) // 200 HTTP code.
                .andExpect(content().string(containsString(
                        "<Set>"
                                + "<item>ECecillo</item>"
                                + "<item>Susan</item>"
                                + "<item>John</item>"
                                + "</Set>"))
                );
    }

    @Test
    @Order(2)
    public void testCreateUserJSON() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"login\":\"newUserJSON\","
                                + "\"password\":\"password\","
                                + "\"species\":\"POULE\","
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
                        + "\"John\"]"))
                ); // Check user correctly added.
    }

    @Test
    @Order(2)
    public void testCreateUserXML() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_XML)
                        .content("<user>"
                                + "<login>newUserXML</login>"
                                + "<password>password</password>"
                                + "<species>POULE</species>"
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
                                + "<item>John</item>"
                                + "</Set>"))
                ); // Check user correctly added.
    }

    @Test
    @Order(3)
    public void testGetUserJSON() throws Exception {
        mockMvc.perform(get("/users/{name}", "newUserJSON")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 HTTP code
                .andExpect(content().string(containsString(
                        "{\"login\":\"newUserJSON\","
                        + "\"species\":\"POULE\","
                        + "\"connected\":false,"
                        + "\"image\":\"imageUrl\"}"))
                );
    }

    @Test
    @Order(3)
    public void testGetUserXML() throws Exception {
        mockMvc.perform(get("/users/{name}", "newUserXML")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk()) // 200 HTTP code
                .andExpect(content().string(containsString(
                        "<User>"
                        + "<login>newUserXML</login>"
                        + "<species>POULE</species>"
                        + "<connected>false</connected>"
                        + "<image>imageUrl</image>"
                        + "</User>"))
                );
    }
    @Test
    public void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/users/{name}", "notExist"))
                .andExpect(status().isNotFound()); // 404 HTTP code
    }

    @Test
    @Order(4)
    public void testDeleteUserJSON() throws Exception {
        mockMvc.perform(delete("/users/{name}", "newUserJSON")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // 204 HTTP code.
                .andExpect(content().string(containsString("User deleted successfully")));
        mockMvc.perform(get("/users"))
                .andExpect(content().string(containsString("[\"newUserXML\",\"ECecillo\",\"Susan\",\"John\"]"))
                ); // User deleted.
    }

    @Test
    @Order(4)
    public void testDeleteUserXML() throws Exception {
        mockMvc.perform(delete("/users/{name}", "newUserXML")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNoContent()) // 204 HTTP code.
                .andExpect(content().string(containsString("User deleted successfully")));
        mockMvc.perform(get("/users"))
                .andExpect(content().string(containsString("[\"ECecillo\",\"Susan\",\"John\"]"))
                ); // User deleted.
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/users/{name}", "notExist"))
                .andExpect(status().isNotFound()); // 404 HTTP code user not found.
    }

    @Test
    public void testCreateUserAlreadyExist() throws Exception {
        String json = "{"
                + "\"login\":\"Susan\","
                + "\"password\":\"password\","
                + "\"species\":\"POULE\","
                + "\"image\":null"
                + "}";
        String xml = "<user>"
                + "<login>Susan</login>"
                + "<password>password</password>"
                + "<species>POULE</species>"
                + "<image>imageUrl</image>"
                + "</user>";
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict()); // 409 HTTP code failed created user JSON format.
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_XML).content(xml))
                .andExpect(status().isConflict()); // 409 HTTP code failed created user XML format.
    }

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

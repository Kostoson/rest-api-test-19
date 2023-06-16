package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class ReqresApiTests {

    String reqresInUri = "https://reqres.in/api/";
    String requestBobyForCreate = "{\n" +
            "    \"name\": \"Konstantin\",\n" +
            "    \"job\": \"QA\"\n" +
            "}";
    String requestBobyForLogin = "{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n" +
            "    \"password\": \"cityslicka\"\n" +
            "}";
    String requestBobyForLoginWithoutEmail = "{\n" +
            "    \"password\": \"cityslicka\"\n" +
            "}";


    @Test
    @Tag("Positive")
    @DisplayName("checking the email of an existing user")
    void checkEmailForExistingSingleUser() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get(reqresInUri + "users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("jsonschemes/single-user-json-schema.json"))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    @Tag("Negative")
    @DisplayName("checking the email for does not existing user")
    void checkEmailForDoesNotExistingSingleUser() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get(reqresInUri + "users/133")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Create user")
    void createUser() {
        given()
                .log().uri()
                .contentType(JSON)
                .body(requestBobyForCreate)
                .when()
                .post(reqresInUri + "users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("jsonschemes/create-json-schema.json"))
                .body("name", is("Konstantin"))
                .body("job", is("QA"));
    }

    @Test
    @Tag("Positive")
    @DisplayName("Login user")
    void loginUser() {
        given()
                .log().uri()
                .contentType(JSON)
                .body(requestBobyForLogin)
                .when()
                .post(reqresInUri + "login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
    @Test
    @Tag("Negative")
    @DisplayName("Login user without email in request body")
    void loginUserWithoutEmail() {
        given()
                .log().uri()
                .contentType(JSON)
                .body(requestBobyForLoginWithoutEmail)
                .when()
                .post(reqresInUri + "login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }
}

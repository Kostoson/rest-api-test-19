package tests.reqres;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;


public class ReqresApiTests {

    @Test
    @Tag("Positive")
    @DisplayName("checking the email of an existing user")
    void checkEmailForExistingSingleUser() {
        String email = "janet.weaver@reqres.in";
        SingleUserResponse response = step("Make the request", () ->
                given(requestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(response200)
                        .body(matchesJsonSchemaInClasspath("jsonschemes/single-user-json-schema.json"))
                        .extract().as(SingleUserResponse.class));
        step("Check name in response", () ->
                assertEquals(email, response.getData().getEmail()));
    }

    @Test
    @Tag("Negative")
    @DisplayName("checking the email for does not existing user")
    void checkEmailForDoesNotExistingSingleUser() {
        RestAssured.filters(new AllureRestAssured());
        step("Make the request and checking status code", () ->
        given(requestSpec)
                .log().uri()
                .contentType(JSON)
                .when()
                .get("/users/133")
                .then()
                .spec(response404SingleUser));
    }

    @Test
    @Tag("Positive")
    @DisplayName("Create user")
    void createUser() {
        CreateRequestBody createRequestBody = new CreateRequestBody();
        createRequestBody.setName("Konstantin");
        createRequestBody.setJob("QA");
        CreateResponseBody createResponseBody = step("Make the request", () ->

        given(requestSpec)
                .body(createRequestBody)
                .when()
                .post("/users")
                .then()
                .spec(response201CreateUser)
                .body(matchesJsonSchemaInClasspath("jsonschemes/create-json-schema.json"))
                .extract().as(CreateResponseBody.class));
        step("Check name in response", () ->
                assertThat(createResponseBody.getName()).isEqualTo("Konstantin"));
        step("Check job in response", () ->
                assertThat(createResponseBody.getJob()).isEqualTo("QA"));
        step("Check id in response", () ->
                assertThat(createResponseBody.getId()).isNotNull());
        step("Check createdAt in response", () ->
                assertThat(createResponseBody.getCreatedAt()).isNotNull());

    }

    @Test
    @Tag("Positive")
    @DisplayName("Login user")
    void loginUser() {

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail("eve.holt@reqres.in");
        loginRequestBody.setPassword("cityslicka");
        LoginResponseBody loginResponseBody = step("Make the request", () ->

                given(requestSpec)
                        .body(loginRequestBody)
                        .when()
                        .post("/login")
                        .then()
                        .spec(response200)
                        .extract().as(LoginResponseBody.class));

        step("Check token in response", () ->
                assertThat(loginResponseBody.getToken()).isNotNull());
    }



    @Test
    @Tag("Negative")
    @DisplayName("Login user without email in request body")
    void loginUserWithoutEmail() {

        LoginRequestBodyWithoutEmail requestBody = new LoginRequestBodyWithoutEmail();
        requestBody.setPassword("cityslicka");
        LoginResponseBodyWithoutEmail responseBody = step("Make the request", () ->
        given(requestSpec)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .spec(response400CreateUser)
                .extract().as(LoginResponseBodyWithoutEmail.class));

        step("Check error in response", () ->
                assertThat(responseBody.getError()).isEqualTo("Missing email or username"));

    }
}

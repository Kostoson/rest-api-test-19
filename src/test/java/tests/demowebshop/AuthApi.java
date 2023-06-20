package tests.demowebshop;

import static io.restassured.RestAssured.given;

public class AuthApi {
    public String getAuthCookie(String login, String password) {

        String authCookieKey = "NOPCOMMERCE.AUTH";

        return given()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie(authCookieKey);
    }
}



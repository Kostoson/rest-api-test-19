package tests.demowebshop;


import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class LoginTests extends TestBase{

    @Test
    void loginWithUITest() {
        step("Open login page", () ->
                open("/login"));
        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password).pressEnter();
        });
        step("Verify successful authorization", () -> {
            $(".account").shouldHave(text(login));
        });
    }

    @Test
    void loginWithApiTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authCookieValue = given()
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract()
                    .cookie(authCookieKey);

            open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
            Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
            getWebDriver().manage().addCookie(authCookie);

        });
        step("Open main page", () -> {
            open("");
        });
        step("Verify successful authorization", () -> {
            $(".account").shouldHave(text(login));
        });
    }
}

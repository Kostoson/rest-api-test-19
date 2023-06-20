package tests.demowebshop;

import models.AddToCardResponse;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.logevents.SelenideLogger.step;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;

public class CartTest extends TestBase {
    String authCookieValue;

    int quantity = 3;
    String data = "product_attribute_16_5_4=14" +
            "&product_attribute_16_6_5=15" +
            "&product_attribute_16_3_6=19" +
            "&product_attribute_16_4_7=44" +
            "&product_attribute_16_8_8=22" +
            "&addtocart_16.EnteredQuantity=" + quantity;
    @Test
    void addToCartTest() {

        step("Get authorization cookie by api and set it to browser", () ->
              authCookieValue = authApi.getAuthCookie(login, password));

        AddToCardResponse response = step("Request to add items to cart", () ->
                given()
                        .headers("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie(authCookieKey, authCookieValue)
                        .body(data)
                        .when()
                        .post("/addproducttocart/details/16/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(AddToCardResponse.class));

        step("Check response adding items to cart", () -> {
            assertThat(response.getSuccess()).isEqualTo("true");
            assertThat(response.getMessage()).isEqualTo("The product has been added to your <a href=\"/cart\">shopping cart</a>");
            assertThat(response.getUpdatetopcartsectionhtml()).isEqualTo("(3)");
        });



            //todo check cart size
    }
}

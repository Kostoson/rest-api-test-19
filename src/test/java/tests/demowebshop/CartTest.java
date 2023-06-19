package tests.demowebshop;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

public class CartTest extends TestBase {

    @Test
    void addToCartTest() {

        String authCookieKey = "NOPCOMMERCE.AUTH";
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

        String data = "product_attribute_16_5_4=14" +
                "&product_attribute_16_6_5=15" +
                "&product_attribute_16_3_6=19" +
                "&product_attribute_16_4_7=44" +
                "&product_attribute_16_8_8=22" +
                "&addtocart_16.EnteredQuantity=1";

        given()
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookieKey, authCookieValue)
                .body(data)
                .when()
                .post("/addproducttocart/details/16/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));

    }
}

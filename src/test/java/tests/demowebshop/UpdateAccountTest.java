package tests.demowebshop;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UpdateAccountTest extends  TestBase{
    @Test
    void editAddressTest() {
        Map<String, String> data = new HashMap<>();
        data.put("Address.Id", "3125688");
        data.put("Address.FirstName", "Ridik");
        data.put("Address.LastName", "Vidik");
        data.put("Address.Email", "aut@qa.com");
        data.put("Address.Company", "Comp");
        data.put("Address.CountryId", "1");
        data.put("Address.StateProvinceId", "1");
        data.put("Address.City", "Test");
        data.put("Address.Address1", "Test1");
        data.put("Address.Address2", "Test2");
        data.put("Address.ZipPostalCode", "54321");
        data.put("Address.PhoneNumber", "76665554433");
        data.put("Address.FaxNumber", "1234567");

        String authCookieValue = authApi.getAuthCookie(login, password);

        given()
                .contentType("application/x-www-form-urlencoded")
                .cookie(authCookieKey, authCookieValue)
                .formParams(data)
                .when()
                .post("/customer/addressedit/3125688")
                .then()
                .log().all()
                .statusCode(302);
    }
}

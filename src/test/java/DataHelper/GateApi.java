package DataHelper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;

public final class GateApi {
    @Value
    public static class Card {
        private String number;
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(System.getProperty("api.url"))
            .setPort(Integer.parseInt(System.getProperty("api.port")))
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static String paymentGateRequest(String cardNumber) {
        return
                given()
                        .spec(requestSpec)
                        .body(new Card(cardNumber))
                        .when()
                        .post("/payment")
                        .then()
                        .contentType(ContentType.JSON).extract().response()
                        .jsonPath()
                        .get("status");
    }

    public static String creditGateRequest(String cardNumber) {
        return
                given()
                        .spec(requestSpec)
                        .body(new Card(cardNumber))
                        .when()
                        .post("/credit")
                        .then()
                        .contentType(ContentType.JSON).extract().response()
                        .jsonPath()
                        .get("status");
    }

}
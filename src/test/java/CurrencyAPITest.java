import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CurrencyAPITest {

    final static String ROOT = "http://api.currencylayer.com/live?access_key=";
    final static String HISTORICAL_ROOT = "http://api.currencylayer.com/historical?access_key=";
    final static String KEY = "240f6de08f01dd4943b2cb587736374f";

    @Test
    public void getCorrectStatusCodeTest() {
        Response response = given().get(ROOT+KEY+"&currencies=CAD,EUR,ILS,RUB");
        response.then().log().body();
        response.then().statusCode(200);
    }

    @Test
    public void getStatusCode101Test() {
        Response response = given().get(ROOT+"key");
        response.then().log().body();
        response.then().body("error.code", equalTo(101));
        response.then().body("error.type", equalTo("invalid_access_key"));
    }

    @Test
    public void getHistoricalDateTest() {
        Response response = given().get(HISTORICAL_ROOT+KEY+"&Date=2018-01-27&currencies=CAD,EUR,ILS,RUB");
        response.then().log().body();
        response.then().body("date", equalTo("2018-01-27"));
    }
    @Test
    public void getStatusCode302Test() {
        Response response = given().get(HISTORICAL_ROOT+KEY+"&Date=2020-27-27");
        response.then().log().body();
        response.then().body("error.code", equalTo(302));
        response.then().body("error.info", equalTo("You have not specified a date. [Required format: date=YYYY-MM-DD]"));
    }
    @Test
    public void getStatusCode301Test() {
        Response response = given().get(HISTORICAL_ROOT+KEY+"&Date=");
        response.then().log().body();
        response.then().body("error.code", equalTo(301));
        response.then().body("error.info", equalTo("You have not specified a date. [Required format: date=YYYY-MM-DD]"));
    }



    @Test
    public void getEndPointTest() {
        Response response = given().get(ROOT+KEY+"&currencies=CAD,EUR,ILS,RUB");
        response.then().body("success", notNullValue());
        response.then().body("privacy", notNullValue());
        response.then().body("timestamp", notNullValue());
        response.then().body("source", notNullValue());
        response.then().body("quotes", notNullValue());
        response.then().log().body();
    }

}

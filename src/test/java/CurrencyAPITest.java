import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CurrencyAPITest {

    final static String ROOT = "http://api.currencylayer.com/live?access_key=";
    final static String HISTORICAL_ROOT = "http://api.currencylayer.com/historical?access_key=";
    final static String KEY = "240f6de08f01dd4943b2cb587736374f";


    @Test
    public void getActualCurrencyForOneTest() {
        Response response = given().get(ROOT + KEY + "&currencies=CAD");
        response.then().body("success", notNullValue());
        response.then().body("privacy", notNullValue());
        response.then().body("timestamp", notNullValue());
        response.then().body("source", notNullValue());
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().log().body();
    }
    @Test
    public void getHistoricalCurrencyForOneTest() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=2018-01-27&currencies=CAD");
        response.then().body("success", notNullValue());
        response.then().body("privacy", notNullValue());
        response.then().body("timestamp", notNullValue());
        response.then().body("source", notNullValue());
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().log().body();
    }
    @Test
    public void getActualCurrencyTest() {
        Response response = given().get(ROOT + KEY + "&currencies=CAD,EUR,ILS,RUB");
        response.then().body("success", notNullValue());
        response.then().body("privacy", notNullValue());
        response.then().body("timestamp", notNullValue());
        response.then().body("source", notNullValue());
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().body("quotes.USDEUR", notNullValue());
        response.then().body("quotes.USDILS", notNullValue());
        response.then().body("quotes.USDRUB", notNullValue());
        response.then().log().body();
    }

    @Test
    public void getHistoricalCurrencyTest() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=2018-01-27&currencies=CAD,EUR,ILS,RUB");
        response.then().body("success", notNullValue());
        response.then().body("privacy", notNullValue());
        response.then().body("timestamp", notNullValue());
        response.then().body("source", notNullValue());
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().body("quotes.USDEUR", notNullValue());
        response.then().body("quotes.USDILS", notNullValue());
        response.then().body("quotes.USDRUB", notNullValue());
        response.then().body("date", equalTo("2018-01-27"));
        response.then().log().body();
    }

    @Test
    public void actualDateValidKeyTest() {
        Response response = given().get(ROOT + KEY);
        response.then().log().body();
        response.then().statusCode(200);
    }
    @Test
    public void historicalDateValidKeyTest() {
        Response response = given().get(HISTORICAL_ROOT + KEY+"&Date=2018-01-28");
        response.then().log().body();
        response.then().statusCode(200);
        response.then().body("historical",equalTo(true));
    }
    @Test
    public void actualDateNotValidKeyTest() {
        Response response = given().get(ROOT + "&Date=2018-01-27&currencies=CAD,EUR,ILS,RUB");
        response.then().log().body();
        response.then().body("error.code", equalTo(101));
        response.then().body("error.type", equalTo("missing_access_key"));
        response.then().body("error.info", equalTo("You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]"));
    }
    @Test
    public void historicalDateNotValidKeyTest() {
        Response response = given().get(HISTORICAL_ROOT + "&Date=2018-01-27&currencies=CAD,EUR,ILS,RUB");
        response.then().log().body();
        response.then().body("error.code", equalTo(101));
        response.then().body("error.type", equalTo("missing_access_key"));
        response.then().body("error.info", equalTo("You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]"));
    }

    @Test
    public void getStatusCode101Test() {
        Response response = given().get(ROOT + "Not valid key");
        response.then().log().body();
        response.then().body("error.code", equalTo(101));
        response.then().body("error.type", equalTo("invalid_access_key"));
    }
    @Test
    public void getStatusCode103Test() {
        Response response = given().get("http://api.currencylayer.com/liver?access_key=" + KEY);
        response.then().log().body();
        response.then().body("error.code", equalTo(103));
        response.then().body("error.info", equalTo("This API Function does not exist."));
    }
    @Test
    public void getStatusCode105Test() {
        Response response = given().get(HISTORICAL_ROOT+ KEY +"&Date=1995-01-01"+"&source=CAD");
        response.then().log().body();
        response.then().body("error.code", equalTo(105));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", equalTo("Access Restricted - Your current Subscription Plan does not support Source Currency Switching."));
    }
    @Test
    public void getStatusCode106Test() {
        Response response = given().get(HISTORICAL_ROOT+ KEY +"&Date=1995-01-01");
        response.then().log().body();
        response.then().body("error.code", equalTo(106));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", equalTo("Your query did not return any results. Please try again."));
    }
    @Test
    public void getStatusCode201Test() {
        Response response = given().get(HISTORICAL_ROOT+ KEY +"&Date=1995-01-01"+"&source=CADA");
        response.then().log().body();
        response.then().body("error.code", equalTo(201));
        response.then().body("success", equalTo(false));
        response.then().body("error.info", equalTo("You have supplied an invalid Source Currency. [Example: source=EUR]"));
    }

    @Test
    public void getStatusCode202Test() {
        Response response = given().get(ROOT + KEY + "&currencies=Not correct currency");
        response.then().log().body();
        response.then().body("error.code", equalTo(202));
        response.then().body("error.info", equalTo("You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"));
    }

    @Test
    public void getStatusCode301Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=");
        response.then().log().body();
        response.then().body("error.code", equalTo(301));
        response.then().body("error.info", equalTo("You have not specified a date. [Required format: date=YYYY-MM-DD]"));
    }

    @Test
    public void getStatusCode302Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=2020-27-27");
        response.then().log().body();
        response.then().body("error.code", equalTo(302));
        response.then().body("error.info", equalTo("You have entered an invalid date. [Required format: date=YYYY-MM-DD]"));
    }


}

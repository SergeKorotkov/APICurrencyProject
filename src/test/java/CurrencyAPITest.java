import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static Constans.Constans.*;


public class CurrencyAPITest {

    @Test
    public void getActualCurrencyForOneTest() {
        Response response = given().get(ROOT + KEY + "&currencies=CAD");
        response.then().body(SUCCESS, notNullValue());
        response.then().body(PRIVACY, notNullValue());
        response.then().body(TIMESTAMP, notNullValue());
        response.then().body(SOURCE, notNullValue());
        response.then().body(CAD_QUOTE, notNullValue());
        response.then().log().body();
    }

    @Test
    public void getHistoricalCurrencyForOneTest() {
        Response response = given().get(HISTORICAL_ROOT + KEY + DATE_QUOTE+"&currencies=CAD");
        response.then().body(SUCCESS, notNullValue());
        response.then().body(PRIVACY, notNullValue());
        response.then().body(TIMESTAMP, notNullValue());
        response.then().body(SOURCE, notNullValue());
        response.then().body(CAD_QUOTE, greaterThan(0f));
        response.then().log().body();
    }

    @Test
    public void getActualCurrencyTest() {
        Response response = given().get(ROOT + KEY + CURRENCY_QUOTE);
        response.then().body(SUCCESS, notNullValue());
        response.then().body(PRIVACY, notNullValue());
        response.then().body(TIMESTAMP, notNullValue());
        response.then().body(SOURCE, notNullValue());
        response.then().body(CAD_QUOTE, greaterThan(0f));
        response.then().body(EUR_QUOTE, greaterThan(0f));
        response.then().body(ILS_QUOTE, greaterThan(0f));
        response.then().body(RUB_QUOTE, greaterThan(0f));
        response.then().log().body();
    }

    @Test
    public void getHistoricalCurrencyTest() {
        Response response = given().get(HISTORICAL_ROOT + KEY + DATE_QUOTE+CURRENCY_QUOTE);
        response.then().body(SUCCESS, notNullValue());
        response.then().body(PRIVACY, notNullValue());
        response.then().body(TIMESTAMP, notNullValue());
        response.then().body(SOURCE, notNullValue());
        response.then().body(CAD_QUOTE, greaterThan(0f));
        response.then().body(EUR_QUOTE, greaterThan(0f));
        response.then().body(ILS_QUOTE, greaterThan(0f));
        response.then().body(RUB_QUOTE, greaterThan(0f));
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
        Response response = given().get(HISTORICAL_ROOT + KEY + DATE_QUOTE);
        response.then().log().body();
        response.then().statusCode(200);
        response.then().body("historical", equalTo(true));
    }

    @Test
    public void actualDateNotValidKeyTest() {
        Response response = given().get(ROOT + DATE_QUOTE+CURRENCY_QUOTE);
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(101));
        response.then().body(ERROR_TYPE, equalTo("missing_access_key"));
        response.then().body(ERROR_INFO, equalTo("You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]"));
    }

    @Test
    public void historicalDateNotValidKeyTest() {
        Response response = given().get(HISTORICAL_ROOT + DATE_QUOTE+CURRENCY_QUOTE);
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(101));
        response.then().body(ERROR_TYPE, equalTo("missing_access_key"));
        response.then().body(ERROR_INFO, equalTo("You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]"));
    }

    @Test
    public void getStatusCode101Test() {
        Response response = given().get(ROOT + "Not valid key");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(101));
        response.then().body(ERROR_TYPE, equalTo("invalid_access_key"));
    }

    @Test
    public void getStatusCode103Test() {
        Response response = given().get("http://api.currencylayer.com/liver?access_key="+KEY);
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(103));
        response.then().body(ERROR_INFO, equalTo("This API Function does not exist."));
    }

    @Test
    public void getStatusCode105Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=1995-01-01" + "&source=CAD");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(105));
        response.then().body(SUCCESS, equalTo(false));
        response.then().body(ERROR_INFO, equalTo("Access Restricted - Your current Subscription Plan does not support Source Currency Switching."));
    }

    @Test
    public void getStatusCode106Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=1995-01-01");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(106));
        response.then().body(SUCCESS, equalTo(false));
        response.then().body(ERROR_INFO, equalTo("Your query did not return any results. Please try again."));
    }

    @Test
    public void getStatusCode201Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + DATE_QUOTE+ "&source=CADA");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(201));
        response.then().body(SUCCESS, equalTo(false));
        response.then().body(ERROR_INFO, equalTo("You have supplied an invalid Source Currency. [Example: source=EUR]"));
    }

    @Test
    public void getStatusCode202Test() {
        Response response = given().get(ROOT + KEY + "&currencies=Not correct currency");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(202));
        response.then().body(ERROR_INFO, equalTo("You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"));
    }

    @Test
    public void getStatusCode301Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(301));
        response.then().body(ERROR_INFO, equalTo("You have not specified a date. [Required format: date=YYYY-MM-DD]"));
    }

    @Test
    public void getStatusCode302Test() {
        Response response = given().get(HISTORICAL_ROOT + KEY + "&Date=2020-27-27");
        response.then().log().body();
        response.then().body(ERROR_CODE, equalTo(302));
        response.then().body(ERROR_INFO, equalTo("You have entered an invalid date. [Required format: date=YYYY-MM-DD]"));
    }


}

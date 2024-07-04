import config.*;
import io.restassured.RestAssured;
import config.OrderAPI;

import config.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class TestOrdersListInResponse {
    // Класс тестирует эндпойнт /api/v1/orders, на данном этапе происходит проверка того, что в тело ответа
    // возврашается список заказов.

    @Before  // Задаем базовый URI
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;

    }
    @Test
    public void TestOrdersListInResponse(){
        Response response = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .get(OrderAPI.getOrdersListAPIPath);
        response.then().assertThat().body("orders",notNullValue());
    }

}
import config.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import static org.apache.http.HttpStatus.*;

import io.restassured.response.Response;
import io.restassured.RestAssured;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;


public class TestLoginCourier {
    // Данный класс содержит позитивные и негативные проверки эндпойнта POST /api/v1/courier/login (логин курьера в системе)
    Courier courier;

    @Before  // Задаем базовый URI и создаем экземпляр класса Courier
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Courier can log in")
    @Description("Courier can log in with all required data. We get courier id in response body")
    public void testCanLogInWithProperRequiredFields() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Courier cannot get in app without Login")
    @Description("Courier cannot get in app without Login. He gets 400 Bad request")
    public void testCannotGetInAppWithoutLogin() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String loginKeeper = courier.getLogin();
        courier.setLogin("");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app without a Password")
    @Description("Courier cannot get in app without a Password. He gets 400 Bad request")
    public void testCannotGetInAppWithoutPassword() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        courier.setPassword("");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app with wrong Login")
    @Description("Courier cannot get in app with wrong Login. He gets 404 Not found")
    public void testCannotGetInAppWithWrongLogin() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String loginKeeper = courier.getLogin();
        courier.setLogin("WrongLogin");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Courier cannot get in app with a wrong Password")
    @Description("Courier cannot get in app with a wrong Password. He gets 404 Not found")
    public void testCannotGetInAppWithWrongPassword() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        courier.setPassword("WrongPwd");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Non-existent courier cannot get in app")
    @Description("Courier cannot get in app with a wrong Password and Login. He gets 404 Not found")
    public void testNonExistentCourierCannotLogIn() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        String loginKeeper = courier.getLogin();
        courier.setPassword("WrongPwd");
        courier.setLogin("WrongLogin");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
        courier.setLogin(loginKeeper);
    }

    @After
    @Description("Deletion of a courier if exists")
    public void deleteTestCourierIfExist() {
        if (courier.isInApp()) {
            CourierAPI.loginCourier(courier);
            CourierAPI.deleteCourier(courier);
        }
    }


}
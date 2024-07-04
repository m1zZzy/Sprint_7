import config.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.apache.http.HttpStatus.*;

import static io.restassured.RestAssured.*;

public class TestCreateCourier {
    // Данный класс содержит позитивные и негативные проверки эндпойнта POST /api/v1/courier (создание курьера)
    Courier courier;

    @Before  // Задаем базовый URI и создаем экземпляр класса Courier
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Create new courier")
    @Description("A new courier can be created")
    public void testCanCreateNewCourier() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test 2 identical courier")
    @Description("Test that 2 identical couriers can not be created")
    public void testCanNotCreateTwoIdenticalCouriers() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        responseCreate = CourierAPI.createCourier(courier);
        responseCreate.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Test 2 identical logins")
    @Description("Test that 2 couriers with identical logins can not be created")
    public void testCanNotCreateTwoCouriersWithIdenticalLogins() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        String pwdKeeper = courier.getPassword();
        courier.setPassword("newUnusedPwd");
        String firstNameKeeper = courier.getFirstName();
        courier.setFirstName("newUnusedName");
        responseCreate = CourierAPI.createCourier(courier);
        responseCreate.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courier.setPassword(pwdKeeper);
        courier.setFirstName(firstNameKeeper);
    }

    @Test
    @DisplayName("Cannot create courier without login")
    @Description("Courier without login can't be created ")
    public void testCanNotCreateCourierWithoutLogin() {

        String loginKeeper = courier.getLogin();
        courier.setLogin("");

        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(loginKeeper);

    }

    @Test
    @DisplayName("Cannot create courier without password")
    @Description("Courier without password can't be created ")
    public void testCanNotCreateCourierWithoutPassword() {

        String pwdKeeper = courier.getPassword();
        courier.setPassword("");

        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(pwdKeeper);
    }

    @After
    @Description("Deletion of a courier if exists")
    public void deleteTestCourierIfExist() {
        if(courier.isInApp()) {
            CourierAPI.loginCourier(courier);
            CourierAPI.deleteCourier(courier);
        }
    }

}

package config;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAPI {
    //Класс содержит эндпойнты в виде переменных и шаги в виде методов для создания, входа в систему и удаления курьера

    public static final String newCourierAPIPath = "/api/v1/courier";
    public static final String loginCourierAPIPath = "/api/v1/courier/login";
    public static final String deleteCourierAPIPath = "/api/v1/courier/";

    @Step("Create new courier")
    public static Response createCourier(Courier courier) {
        Response responseCreate = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .body(courier)     //.getNewCourierRequestBody())
                .when()
                .post(newCourierAPIPath);
        return responseCreate;
    }

    @Step("Login courier and get Id")
    @Description("Courier logining and getting courier's id")
    public static Response loginCourier(Courier courier) {
        Response responseLogin =
                given()
                        .header(Data.REQUEST_HEADER)
                        .and()
                        .body(courier)                               //.getLoginCourierRequestBody())
                        .when()
                        .post(loginCourierAPIPath);
        return responseLogin;
    }

    @Step("Delete courier")
    public static void deleteCourier(Courier courier) {
        try {
            Response responseLogin = loginCourier(courier);
            int courierId = responseLogin.jsonPath().getInt("id");
            given()
                    .header(Data.REQUEST_HEADER)
                    .when()
                    .delete(deleteCourierAPIPath + courierId);

        } catch (NullPointerException e) {
            System.out.println("Курьер не был создан, его невозможно удалить!");
        }

    }
}

package config;

public class OrderAPI {
    // Класс содержит эндпойнты в виде переменных для работы со списком заказов
    public static final String getOrdersListAPIPath =  "/api/v1/orders";
    public static final String newOrderPath = "/api/v1/orders";

    public static String getOrderByIdPath(int orderId) {
        return "/api/v1/orders/track?t="+orderId;
    }

    public static String getFinishOrderPath(int orderId) {
        return "/api/v1/orders/finish/"+orderId;
    }
}
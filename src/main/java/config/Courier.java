package config;

public class Courier {
    // Класс Courier представляет собой абстракцию курьера в системе
    private String login;
    private String password;
    private String firstName;
    boolean isInApp; // поле isInApp необходимо для корректной работы создания, входа и удаления курьера в системе.

    public Courier() {
        this.login = "nintiago";
        this.password = "123499";
        this.firstName = "saske";
        this.isInApp = false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isInApp() {
        return isInApp;
    }

    public void setInApp(boolean inApp) {
        isInApp = inApp;
    }
}
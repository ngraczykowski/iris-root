package steps.frontend;

import io.cucumber.java8.En;

import static com.codeborne.selenide.Selenide.open;
import static utils.pom.LoginPage.loginAsUserViaFrontend;

public class LoginSteps implements En {
  public LoginSteps() {
    Given("Login as admin", () -> {
      open(System.getProperty("test.url"));
      loginAsUserViaFrontend(System.getProperty("test.admin.username"), System.getProperty("test.admin.password"));
    });
  }
}

package steps.frontend;

import com.codeborne.selenide.Selenide;
import io.cucumber.java8.En;

import static com.codeborne.selenide.Selenide.open;
import static utils.pom.LoginPage.loginAsUserViaFrontend;

public class LoginSteps implements En {
  public LoginSteps() {
    Given("User login as admin", () -> {
      //Temporary workaround for session clear
      try {
        Selenide.webdriver().object().close();
      } catch (Exception e) {
        System.out.println("Webdriver is not alive");
      }
      open(System.getProperty("test.url"));
      loginAsUserViaFrontend(System.getProperty("test.admin.username"), System.getProperty("test.admin.password"));
    });
  }
}

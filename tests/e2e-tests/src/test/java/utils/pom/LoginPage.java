package utils.pom;

import com.codeborne.selenide.SelenideElement;

import lombok.experimental.UtilityClass;

import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class LoginPage {

  private final SelenideElement loginInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement loginButton = $("#kc-login");

  public static void loginAsUserViaFrontend(String username, String password) {
    loginInput.setValue(username);
    passwordInput.setValue(password);
    loginButton.click();
  }
}

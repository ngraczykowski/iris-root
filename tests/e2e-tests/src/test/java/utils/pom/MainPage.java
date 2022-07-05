package utils.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import lombok.experimental.UtilityClass;

import org.junit.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@UtilityClass
public class MainPage {

  private final SelenideElement mainGrid = $("sep-app-main-grid");

  public static void userIsOnMainPage() {
    mainGrid.shouldBe(Condition.visible);
    Assert.assertTrue(mainGrid.isDisplayed());
  }

  public static void clickOnSidenavButtonWithText(String value) {
    SelenideElement button = $("sep-app-sidenav").$(byText(value));
    button.shouldBe(Condition.visible);
    button.click();
  }
}

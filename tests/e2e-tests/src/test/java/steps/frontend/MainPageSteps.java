package steps.frontend;

import io.cucumber.java8.En;
import utils.pom.MainPage;

public class MainPageSteps implements En {
  public MainPageSteps() {
    Then("User is on main page", MainPage::userIsOnMainPage);
    And("User clicks policies button", MainPage::clickOnPoliciesButton);
  }
}

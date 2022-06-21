package steps.frontend;

import io.cucumber.java8.En;
import utils.pom.PoliciesPage;

public class PoliciesPageSteps implements En {
  public PoliciesPageSteps() {
    And("User is on policies page", PoliciesPage::userIsOnPoliciesPage);
    And("User clicks on new policy button", PoliciesPage::userClicksOnNewPolicyButton);
    And("User fills policy name input with {string}", PoliciesPage::userFillsPolicyNameWith);
    And("User fills policy description input with {string}", PoliciesPage::userFillsPolicyDescWith);
    And("User clicks on steps tab button", PoliciesPage::userClicksOnStepsTabButton);
    And("User clicks on add new step button", PoliciesPage::userClicksOnAddNewStepButton);
    And("User fills step name input with {string}", PoliciesPage::userFillsStepNameWith);
    And("User fills step description input with {string}", PoliciesPage::userFillsStepDescWith);
    And("User set s8 solution select as {string}", PoliciesPage::userSetS8SolutionSelect);
    And("User clicks on add new rule button", PoliciesPage::userClicksOnAddNewRuleButton);
    And("User set feature name select as {string}", PoliciesPage::userSetFeatureNameSelect);
    And("User set feature condition select as {string}", PoliciesPage::userSetFeatureConditionSelect);
    And("User set feature value select as {string}", PoliciesPage::userSetFeatureValueSelect);
    And("User clicks on mark as ready button", PoliciesPage::userClicksOnMarkAsReadyButton);
    And("User clicks on mark as ready confirmation button", PoliciesPage::userClicksOnMarkAsReadyConfirmationButton);
    Then("User sees success prompt with text {string}", PoliciesPage::userSeesSuccessPromptWithText);
  }
}

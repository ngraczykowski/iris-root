package utils.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import lombok.experimental.UtilityClass;

import org.junit.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static utils.CommonUtils.setMatSelectAs;

@UtilityClass
public class PoliciesPage {

  private final SelenideElement policiesPageLabel = $("#policies-title");
  private final SelenideElement createNewPolicyButton = $(byText("Create New Policy"));
  private final SelenideElement addNewStepButton = $(byText("Add New Step"));
  private final SelenideElement stepsTab = $("#policy-steps-tab");
  private final SelenideElement policyNameInput = $("#policy-name-input");
  private final SelenideElement policyDescInput = $("#policy-description-input");
  private final SelenideElement stepNameInput = $("#policy-step-name-input");
  private final SelenideElement stepDescInput = $("#policy-step-description-input");
  private final SelenideElement s8SolutionSelect = $("#policy-step-solution-select");
  private final SelenideElement addNewRuleButton = $(byText("Add New Rule"));
  private final SelenideElement featureNameSelect = $("#mat-select-12");
  private final SelenideElement featureConditionSelect = $("#mat-select-8");
  private final SelenideElement featureValueSelect = $("#mat-select-10");
  private final SelenideElement markAsReadyButton =  $(byText("Mark as Ready"));
  private final SelenideElement markAsReadyConfirmationButton =  $("mat-dialog-container").$(byText("Mark as Ready"));

  public static void userIsOnPoliciesPage() {
    policiesPageLabel.shouldBe(Condition.visible);
    Assert.assertTrue(policiesPageLabel.isDisplayed());
  }

  public static void userClicksOnNewPolicyButton() {
    createNewPolicyButton.shouldBe(Condition.visible);
    createNewPolicyButton.click();
  }

  public static void userFillsPolicyNameWith(String value) {
    policyNameInput.shouldBe(Condition.visible);
    policyNameInput.clear();
    policyNameInput.sendKeys(value);
  }

  public static void userFillsPolicyDescWith(String value) {
    policyDescInput.shouldBe(Condition.visible);
    policyDescInput.sendKeys(value);
  }

  public static void userClicksOnStepsTabButton() {
    stepsTab.shouldBe(Condition.visible);
    stepsTab.click();
  }

  public static void userClicksOnAddNewStepButton() {
    addNewStepButton.shouldBe(Condition.visible);
    addNewStepButton.click();
  }

  public void userFillsStepNameWith(String value) {
    stepNameInput.shouldBe(Condition.visible);
    stepNameInput.sendKeys(value);
  }

  public void userFillsStepDescWith(String value) {
    stepDescInput.shouldBe(Condition.visible);
    stepDescInput.sendKeys(value);
  }

  public void userSetS8SolutionSelect(String value) {
    setMatSelectAs(s8SolutionSelect, value);
  }

  public void userClicksOnAddNewRuleButton() {
    addNewRuleButton.shouldBe(Condition.visible);
    addNewRuleButton.click();
  }

  public void userSetFeatureNameSelect(String value) {
    setMatSelectAs(featureNameSelect, value);
  }

  public void userSetFeatureConditionSelect(String value) {
    setMatSelectAs(featureConditionSelect, value);
  }

  public void userSetFeatureValueSelect(String value) {
    setMatSelectAs(featureValueSelect, value);
  }

  public void userClicksOnMarkAsReadyButton() {
    markAsReadyButton.shouldBe(Condition.visible);
    markAsReadyButton.click();
  }

  public void userClicksOnMarkAsReadyConfirmationButton() {
    markAsReadyConfirmationButton.shouldBe(Condition.visible);
    markAsReadyConfirmationButton.click();
  }

  public void userSeesSuccessPromptWithText(String value) {
    SelenideElement successPrompt = $(byText(value));
    successPrompt.shouldBe(Condition.visible);
    Assert.assertTrue(successPrompt.isDisplayed());
  }
}

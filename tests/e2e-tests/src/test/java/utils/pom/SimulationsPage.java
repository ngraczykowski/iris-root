package utils.pom;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import lombok.experimental.UtilityClass;

import org.junit.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static utils.CommonUtils.getFullFormatDateWithOffset;
import static utils.CommonUtils.setMatSelectAs;

@UtilityClass
public class SimulationsPage {

  private final SelenideElement simulationsPageLabel = $("sep-app-simulation-list");
  private final SelenideElement createSimulationButton = $(byText("Create Simulation"));
  private final SelenideElement simulationNameInput = $("[formcontrolname='name']");
  private final SelenideElement simulationDescInput = $("[formcontrolname='description']");
  private final SelenideElement policyIdSelect = $("[formcontrolname='policyId']");
  private final SelenideElement datasetSelect = $("[formcontrolname='datasetNames']");
  private final SelenideElement runSimulationButton = $(byText("Run Simulation"));



  public static void userIsOnSimulationsPage() {
    simulationsPageLabel.shouldBe(Condition.exist);
    Assert.assertTrue(simulationsPageLabel.isDisplayed());
  }

  public static void userClicksOnCreateSimulationButton() {
    createSimulationButton.shouldBe(Condition.visible);
    createSimulationButton.click(ClickOptions.usingJavaScript());
  }

  public static void userFillsSimulationNameWith(String value) {
    simulationNameInput.shouldBe(Condition.exist);
    simulationNameInput.sendKeys(value);
  }

  public static void userFillsSimulationDescWith(String value) {
    simulationDescInput.shouldBe(Condition.exist);
    simulationDescInput.sendKeys(value);
  }

  public static void setPolicyIdAs(String value) {
    policyIdSelect.shouldBe(Condition.visible);
    setMatSelectAs(policyIdSelect, value);
  }

  public static void setDatasetAs(String value) {
    datasetSelect.shouldBe(Condition.visible);
    setMatSelectAs(datasetSelect, value);
  }

  public static void userClicksOnRunSimulationButton() {
    runSimulationButton.shouldBe(Condition.visible);
    runSimulationButton.click();
  }

  public static void simulationWithNameDisplayedOnList(String value) {
    SelenideElement simulation = $(byText(value));
    simulation.shouldBe(Condition.visible);
    Assert.assertTrue(simulation.isDisplayed());
  }
}

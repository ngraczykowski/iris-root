package utils.pom;

import com.codeborne.selenide.ClickOptions;

import lombok.experimental.UtilityClass;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static utils.CommonUtils.getDateTimeNow;
import static utils.CommonUtils.getFullFormatDateWithOffset;
import static utils.CommonUtils.getOnlyDateWithOffset;
import static utils.CommonUtils.setMatSelectAs;

@UtilityClass
public class DatasetsPage {

  private final SelenideElement datasetsPageLabel = $("sep-app-dataset-list");
  private final SelenideElement addDatasetButton = $(byText("Add Dataset"));
  private final SelenideElement datasetNameInput = $("#mat-input-0");
  private final SelenideElement datasetDateRange = $("[data-mat-calendar='mat-datepicker-0']");
  private final SelenideElement saveDatasetButton = $(byText("Save Dataset"));



  public static void userIsOnDatasetsPage() {
    datasetsPageLabel.shouldBe(Condition.exist);
    Assert.assertTrue(datasetsPageLabel.isDisplayed());
  }

  public static void userClicksOnAddDatasetButton() {
    addDatasetButton.shouldBe(Condition.visible);
    addDatasetButton.click();
  }

  public static void userFillsDatasetNameWith(String value) {
    datasetNameInput.shouldBe(Condition.exist);
    datasetNameInput.sendKeys(value);
  }

  public static void setDateRangeAsToday() {
    datasetDateRange.shouldBe(Condition.visible);
    datasetDateRange.click(ClickOptions.usingJavaScript());
    $(String.format("button[aria-label='%s' i]", getFullFormatDateWithOffset(-1))).click();
    $(String.format("button[aria-label='%s' i]", getFullFormatDateWithOffset(0))).click();
  }

  public static void userClicksOnSaveDatasetButton() {
    saveDatasetButton.shouldBe(Condition.visible);
    saveDatasetButton.click();
  }
}

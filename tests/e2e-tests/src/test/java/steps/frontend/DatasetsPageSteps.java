package steps.frontend;

import io.cucumber.java8.En;
import utils.pom.DatasetsPage;

public class DatasetsPageSteps implements En {
  public DatasetsPageSteps() {
    And("User is on datasets page", DatasetsPage::userIsOnDatasetsPage);
    And("User clicks on add dataset button", DatasetsPage::userClicksOnAddDatasetButton);
    And("User fills dataset name with {string}", DatasetsPage::userFillsDatasetNameWith);
    And("User set date range as today", DatasetsPage::setDateRangeAsToday);
    And("User clicks on save dataset button", DatasetsPage::userClicksOnSaveDatasetButton);
  }
}

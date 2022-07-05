package steps.frontend;

import io.cucumber.java8.En;
import utils.pom.SimulationsPage;

public class SimulationsPageSteps implements En {
  public SimulationsPageSteps() {
    And("User is on simulations page", SimulationsPage::userIsOnSimulationsPage);
    And("User clicks on create simulation button", SimulationsPage::userClicksOnCreateSimulationButton);
    And("User fills simulation name with {string}", SimulationsPage::userFillsSimulationNameWith);
    And("User fills simulation desc with {string}", SimulationsPage::userFillsSimulationDescWith);
    And("User set policy id as {string}", SimulationsPage::setPolicyIdAs);
    And("User set dataset as {string}", SimulationsPage::setDatasetAs);
    And("User clicks on run simulation button", SimulationsPage::userClicksOnRunSimulationButton);
    And("Simulation with name {string} is displayed on list", SimulationsPage::simulationWithNameDisplayedOnList);
  }
}

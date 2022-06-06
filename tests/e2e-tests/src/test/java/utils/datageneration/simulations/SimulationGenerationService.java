package utils.datageneration.simulations;

import lombok.experimental.UtilityClass;

import steps.Hooks;
import utils.ScenarioContext;
import utils.datageneration.governance.CreateSolvingModel;

import java.util.Arrays;
import java.util.UUID;

@UtilityClass
public class SimulationGenerationService {

  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public static CreateDataset createDataset(String name, String from, String to) {

    return CreateDataset
        .builder()
        .id(UUID.randomUUID().toString())
        .datasetName(name)
        .description("Lorem ipsum dolor sit amet")
        .query(
            Query.builder()
                .alertGenerationDate(AlertGenerationDate.builder()
                    .from(from)
                    .to(to)
                    .build())
                .build())
        .build();
  }

  public static CreateSimulation createSimulation(String name) {
    CreateDataset dataset = (CreateDataset) scenarioContext.get("dataset");
    CreateSolvingModel solvingModel = (CreateSolvingModel) scenarioContext.get("solvingModel");

    return CreateSimulation
        .builder()
        .simulationName(name)
        .description("Lorem ipsum dolor sit amet")
        .datasets(Arrays.asList("datasets/" + dataset.getId()))
        .model("solvingModels/" + solvingModel.getId())
        .id(UUID.randomUUID().toString())
        .build();
  }

}

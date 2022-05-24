package utils.datageneration.simulations;

import com.fasterxml.jackson.databind.ObjectMapper;
import steps.Hooks;
import utils.ScenarioContext;
import utils.datageneration.CommonUtils;
import utils.datageneration.governance.SolvingModel;

import javax.xml.crypto.Data;
import java.util.Map;
import java.util.UUID;

public class SimulationGenerationService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CommonUtils commonUtils = new CommonUtils();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  @SuppressWarnings("unchecked")
  public Dataset generateDataset(String name, String from, String to) {
    Dataset dataset = Dataset.builder()
        .uuid(String.valueOf(UUID.randomUUID()))
        .name(name)
        .from(from)
        .to(to)
        .build();
    dataset.setCreationPayload(commonUtils.template(
        commonUtils.getJsonTemplate("newDataset"), objectMapper.convertValue(dataset, Map.class)));

    return dataset;
  }

  @SuppressWarnings("unchecked")
  public Simulation generateSimulation(String name) {
    Dataset dataset = (Dataset) scenarioContext.get("dataset");
    SolvingModel solvingModel = (SolvingModel) scenarioContext.get("solvingModel");

    Simulation simulation = Simulation.builder()
        .uuid(String.valueOf(UUID.randomUUID()))
        .name(name)
        .description("Lorem ipsum dolor")
        .solvingModel(solvingModel.getUuid())
        .dataset(dataset)
        .build();

    //TODO This is hack, need to figure out how to access object of dataset via StringSubstitor
    Map<String, String> map = objectMapper.convertValue(simulation, Map.class);
    map.put("dataset.uuid", dataset.getUuid());

    simulation.setCreationPayload(commonUtils.template(
        commonUtils.getJsonTemplate("newSimulation"),
        map));

    return simulation;
  }

}

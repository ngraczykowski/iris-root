package com.silenteight.simulator.dataset.create;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.dataset.create")
@ConstructorBinding
class CreateDatasetProperties {

  @NotNull
  private List<@Valid DatasetLabelProperties> labels = emptyList();

  List<DatasetLabel> datasetLabels() {
    return labels
        .stream()
        .map(label -> new DatasetLabel(label.getName(), label.getValues()))
        .collect(toList());
  }
}

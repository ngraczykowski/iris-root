package com.silenteight.connector.ftcc.ingest.state;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toMap;

@Validated
@ConfigurationProperties(prefix = "ftcc.alert.state")
@RequiredArgsConstructor
@ConstructorBinding
class AlertStateProperties {

  @NotNull
  private final Map<State, Set<String>> mappings;

  Map<String, State> stateByStatus() {
    return mappings
        .entrySet()
        .stream()
        .flatMap(
            entry ->
                entry
                    .getValue()
                    .stream()
                    .map(s -> new SimpleEntry<>(entry.getKey(), s)))
        .collect(toMap(SimpleEntry::getValue, SimpleEntry::getKey));
  }
}

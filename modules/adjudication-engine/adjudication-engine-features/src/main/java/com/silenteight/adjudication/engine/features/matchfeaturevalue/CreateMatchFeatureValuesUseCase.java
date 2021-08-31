package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValueDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Collection;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateMatchFeatureValuesUseCase {

  private final MatchFeatureValueRepository repository;
  private final ProtoMessageToObjectNodeConverter converter;

  @Timed(value = "ae.features.use_cases", extraTags = { "package", "matchfeaturevalue" })
  @Transactional
  void createMatchFeatureValues(Collection<MatchFeatureValueDto> valueDtos) {
    repository.saveAll(() -> valueDtos.stream().map(this::createEntity).iterator());
  }

  private MatchFeatureValue createEntity(MatchFeatureValueDto dto) {
    var builder = MatchFeatureValue.builder()
        .id(new MatchFeatureValueKey(dto.getMatchId(), dto.getAgentConfigFeatureId()))
        .value(dto.getValue());

    converter
        .convert(dto.getReason())
        .ifPresentOrElse(builder::reason, () -> log.warn("Failed to convert reason to JSON"));

    return builder.build();
  }
}

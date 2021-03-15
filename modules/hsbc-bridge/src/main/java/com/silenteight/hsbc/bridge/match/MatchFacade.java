package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;

@Builder
public class MatchFacade {

  private final MatchPayloadConverter matchPayloadConverter;
  private final MatchRepository matchRepository;
  private final MatchRawMapper matchRawMapper;

  public MatchComposite getMatch(long id) {
    var matchEntity = matchRepository.findById(id);

    return MatchComposite.builder()
        .id(matchEntity.getId())
        .rawData(matchPayloadConverter.convert(matchEntity.getPayload()))
        .build();
  }

  @Transactional
  public Collection<Long> prepareAndSaveMatches(@NonNull AlertComposite alertComposite) {
    byte[] payload = getPayload(alertComposite.getAlert());
    var matchEntity = new MatchEntity(alertComposite.getId(), payload);

    matchRepository.save(matchEntity);
    return List.of(matchEntity.getId());
  }

  private byte[] getPayload(Alert alert) {
    var matchRawData = matchRawMapper.map(alert);

    return matchPayloadConverter.convert(matchRawData);
  }
}

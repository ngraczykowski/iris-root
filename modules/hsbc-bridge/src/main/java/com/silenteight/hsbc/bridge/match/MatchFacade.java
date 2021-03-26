package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Builder
public class MatchFacade {

  private final MatchPayloadConverter matchPayloadConverter;
  private final MatchRepository matchRepository;
  private final MatchRawMapper matchRawMapper;

  public MatchComposite getMatch(long id) {
    var matchResult = matchRepository.findById(id);

    if (matchResult.isEmpty()) {
      throw new MatchNotFoundException(id);
    }

    var matchEntity = matchResult.get();
    return MatchComposite.builder()
        .id(matchEntity.getId())
        .name(matchEntity.getName())
        .rawData(matchPayloadConverter.convert(matchEntity.getPayload()))
        .build();
  }

  @Transactional
  public Collection<Long> prepareAndSaveMatches(@NonNull AlertComposite alertComposite) {
    var matchName = alertComposite.getCaseId() + "";
    byte[] payload = getPayload(alertComposite.getAlertSystemInformation());
    var matchEntity = new MatchEntity(alertComposite.getId(), matchName, payload);

    matchRepository.save(matchEntity);
    return List.of(matchEntity.getId());
  }

  private byte[] getPayload(AlertSystemInformation alertSystemInformation) {
    var matchRawData = matchRawMapper.map(alertSystemInformation);

    return matchPayloadConverter.convert(matchRawData);
  }

  public List<MatchComposite> getMatches(@NonNull List<Long> matchIds) {
    return matchIds.stream()
        .map(this::getMatch)
        .collect(Collectors.toList());
  }
}

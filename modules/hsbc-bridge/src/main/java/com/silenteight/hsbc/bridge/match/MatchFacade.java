package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
    byte[] payload = getPayload(alertComposite.getAlertSystemInformation());
    var matchEntity = new MatchEntity(alertComposite.getId(), payload);

    matchRepository.save(matchEntity);
    return List.of(matchEntity.getId());
  }

  private byte[] getPayload(AlertSystemInformation alertSystemInformation) {
    var matchRawData = matchRawMapper.map(alertSystemInformation);

    return matchPayloadConverter.convert(matchRawData);
  }

  //validate match id number // throw custom exception
  public List<MatchComposite> getMatches(@NonNull List<String> matchIds) {
    return matchIds.stream()
        .map(Long::valueOf)
        .map(this::getMatch)
        .collect(Collectors.toList());
  }
}

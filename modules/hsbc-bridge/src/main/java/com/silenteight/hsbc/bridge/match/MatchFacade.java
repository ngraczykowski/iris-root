package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Builder
public class MatchFacade {

  private final MatchPayloadConverter matchPayloadConverter;
  private final MatchRepository matchRepository;
  private final MatchRawMapper matchRawMapper;
  private final ApplicationEventPublisher eventPublisher;

  public MatchComposite getMatch(long id) {
    var matchResult = matchRepository.findById(id);

    if (matchResult.isEmpty()) {
      throw new MatchNotFoundException(id);
    }

    var matchEntity = matchResult.get();
    return toMatchComposite(matchEntity);
  }

  public List<MatchComposite> getMatchesByAlertId(long alertId) {
    return toMatchComposites(matchRepository.findMatchEntitiesByAlertId(alertId));
  }

  @Transactional
  public Collection<Long> prepareAndSaveMatches(@NonNull AlertComposite alertComposite) {
    var matchName = alertComposite.getCaseId() + "";
    var matchRawData = matchRawMapper.map(alertComposite.getAlertSystemInformation());
    byte[] payload = getPayload(matchRawData);
    var matchEntity = new MatchEntity(alertComposite.getId(), matchName, payload);

    matchRepository.save(matchEntity);

    eventPublisher.publishEvent(new StoredMatchesEvent(List.of(toMatchComposite(matchEntity))));

    return List.of(matchEntity.getId());
  }

  private byte[] getPayload(MatchRawData matchRawData) {
    return matchPayloadConverter.convert(matchRawData);
  }

  private MatchComposite toMatchComposite(MatchEntity matchEntity) {
    return MatchComposite.builder()
        .id(matchEntity.getId())
        .name(matchEntity.getName())
        .rawData(matchPayloadConverter.convert(matchEntity.getPayload()))
        .build();
  }

  private List<MatchComposite> toMatchComposites(List<MatchEntity> matchEntities) {
    return matchEntities.stream()
        .map(matchEntity -> MatchComposite.builder()
            .id(matchEntity.getId())
            .name(matchEntity.getName())
            .rawData(matchPayloadConverter.convert(matchEntity.getPayload()))
            .build())
        .collect(Collectors.toList());
  }

  public List<MatchComposite> getMatches(@NonNull List<Long> matchIds) {
    return matchIds.stream()
        .map(this::getMatch)
        .collect(Collectors.toList());
  }
}

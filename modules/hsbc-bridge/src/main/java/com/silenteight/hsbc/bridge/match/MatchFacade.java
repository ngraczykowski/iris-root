package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Builder
public class MatchFacade {

  private final MatchPayloadConverter matchPayloadConverter;
  private final MatchRepository matchRepository;
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
  public Collection<MatchIdComposite> prepareAndSaveMatches(
      @NonNull AlertComposite alertComposite) {
    var matches = new RelationshipsProcessor(alertComposite.getAlertRawData()).process();
    var matchComposites = saveMatches(alertComposite.getId(), matches);

    eventPublisher.publishEvent(new StoredMatchesEvent(matchComposites));

    return matchComposites.stream()
        .map(m -> new MatchIdComposite(m.getId(), m.getExternalId()))
        .collect(Collectors.toList());
  }

  public Collection<Long> getMatchIdsByNames(@NonNull List<String> matchValues) {
    return matchRepository.findByNameIn(matchValues).stream()
        .map(MatchEntity::getId)
        .collect(Collectors.toList());
  }

  private List<MatchComposite> saveMatches(long alertId, List<MatchRawData> matches) {
    var matchComposites = new ArrayList<MatchComposite>();
    for (MatchRawData matchRawData : matches) {
      var payload = getPayload(matchRawData);
      var externalId = matchRawData.getCaseId() + "";
      var matchEntity = new MatchEntity(externalId, alertId, payload);

      matchRepository.save(matchEntity);

      matchComposites.add(MatchComposite.builder()
          .id(matchEntity.getId())
          .externalId(matchEntity.getExternalId())
          .rawData(matchRawData)
          .build());
    }

    return matchComposites;
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
        .map(this::toMatchComposite)
        .collect(Collectors.toList());
  }

  public List<MatchComposite> getMatches(@NonNull List<Long> matchIds) {
    return matchIds.stream()
        .map(this::getMatch)
        .collect(Collectors.toList());
  }
}

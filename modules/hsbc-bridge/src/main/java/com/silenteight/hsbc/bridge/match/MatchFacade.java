package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.json.ObjectConverter;
import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch;
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class MatchFacade {

  private final ObjectConverter objectConverter;
  private final MatchDataMapper matchDataMapper;
  private final MatchRepository matchRepository;
  private final ApplicationEventPublisher eventPublisher;

  public List<MatchComposite> getMatchesByAlertId(long alertId) {
    return toMatchComposites(matchRepository.findMatchEntitiesByAlertId(alertId));
  }

  @Transactional
  public Collection<MatchIdComposite> prepareAndSaveMatches(long alertId, List<Match> matches) {
    var matchComposites = saveMatches(alertId, matches);

    eventPublisher.publishEvent(new StoredMatchesEvent(matchComposites));

    return matchComposites.stream()
        .map(m -> new MatchIdComposite(m.getId(), m.getExternalId()))
        .collect(Collectors.toList());
  }

  private List<MatchComposite> saveMatches(long alertId, List<Match> matches) {
    var matchComposites = new ArrayList<MatchComposite>();
    for (Match match : matches) {
      var matchRawData = toMatchRawData(match.getMatchData());
      var payload = getPayload(matchRawData);
      var matchEntity = new MatchEntity(match.getExternalId(), alertId);
      matchEntity.setPayload(new MatchPayloadEntity(payload));

      matchRepository.save(matchEntity);

      matchComposites.add(MatchComposite.builder()
          .id(matchEntity.getId())
          .externalId(matchEntity.getExternalId())
          .matchData(matchRawData)
          .build());
    }

    return matchComposites;
  }

  private MatchRawData toMatchRawData(HsbcMatch hsbcMatch) {
    return matchDataMapper.map(hsbcMatch);
  }

  private byte[] getPayload(MatchRawData matchData) {
    return objectConverter.convert(matchData);
  }

  private MatchComposite toMatchComposite(MatchEntity matchEntity) {
    var payload = matchEntity.getPayload();
    return MatchComposite.builder()
        .id(matchEntity.getId())
        .name(matchEntity.getName())
        .matchData(objectConverter.convert(payload.getPayload(), MatchRawData.class))
        .build();
  }

  private List<MatchComposite> toMatchComposites(List<MatchEntity> matchEntities) {
    return matchEntities.stream()
        .map(this::toMatchComposite)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MatchComposite> getMatches(@NonNull List<String> matchNames) {
    return matchNames.stream()
        .map(this::getMatch)
        .collect(Collectors.toList());
  }

  private MatchComposite getMatch(String name) {
    var matchResult = matchRepository.findByName(name);

    if (matchResult.isEmpty()) {
      throw new MatchNotFoundException(name);
    }

    var matchEntity = matchResult.get();
    return toMatchComposite(matchEntity);
  }
}

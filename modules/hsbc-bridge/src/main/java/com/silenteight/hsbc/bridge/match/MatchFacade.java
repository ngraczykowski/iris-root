package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.hsbc.bridge.json.ObjectConverter;
import com.silenteight.hsbc.bridge.json.ObjectConverter.ObjectConversionException;
import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch;
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
  public void prepareAndSaveMatches(long alertId, List<Match> matches) {
    var matchComposites = saveMatches(alertId, matches);

    eventPublisher.publishEvent(new StoredMatchesEvent(matchComposites));
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
    var payloadEntity = matchEntity.getPayload();

    if (payloadEntity.isArchived()) {
      throw new MatchDataNoLongerAvailableException(matchEntity.getName());
    }

    var name = matchEntity.getName();
    return MatchComposite.builder()
        .id(matchEntity.getId())
        .name(name)
        .externalId(matchEntity.getExternalId())
        .matchData(getMatchRawData(name, payloadEntity.getPayload()))
        .build();
  }

  private MatchRawData getMatchRawData(String name, byte[] payload) {
    try {
      return objectConverter.convert(payload, MatchRawData.class);
    } catch (ObjectConversionException e) {
      throw new MatchDataNoLongerAvailableException(name);
    }
  }

  private List<MatchComposite> toMatchComposites(List<MatchEntity> matchEntities) {
    return matchEntities.stream()
        .map(this::toMatchComposite)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MatchComposite> getMatches(@NonNull List<String> names) {
    return names.stream()
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

  public List<MatchComposite> getMatchesByAlertNames(@NonNull List<String> alerts) {
    return matchRepository.findByAlertNames(alerts)
        .stream()
        .map(this::toMatchComposite)
        .collect(Collectors.toList());
  }
}

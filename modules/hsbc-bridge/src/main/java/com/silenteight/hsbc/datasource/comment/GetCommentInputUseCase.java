package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.datamodel.MatchData;

import one.util.streamex.StreamEx;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Collections.sort;

@Slf4j
@RequiredArgsConstructor
class GetCommentInputUseCase {

  private static final String INDIVIDUAL = "I";
  private static final String ENTITY = "C";
  private final MatchFacade matchFacade;
  private final Map<String, List<String>> wlTypesMap;

  @Transactional(readOnly = true)
  public List<CommentInputDto> getInputRequestsResponse(StreamCommentInputsRequestDto request) {
    log.info("Create comments request received.");
    var alertsSorted = new ArrayList<>(request.getAlerts());
    sort(alertsSorted);

    var matches = matchFacade.getMatchesByAlertNames(alertsSorted);

    return createCommentInputList(matches, alertsSorted);
  }

  private ArrayList<CommentInputDto> createCommentInputList(
      List<MatchComposite> matches, List<String> alertsName) {
    var commentInputs = new ArrayList<CommentInputDto>();

    var index = new AtomicInteger(0);
    StreamEx.of(matches)
        .zipWith(Stream.generate(index::getAndIncrement))
        .forKeyValue((match, idx) -> commentInputs.add(
            CommentInputDto.builder()
                .alert(alertsName.get(idx))
                .alertCommentInput(toAlertCommentMap(match))
                .matchCommentInputsDto(createMatchCommentInputs(match))
                .build()));

    log.info("Comment inputs created.");
    return commentInputs;
  }

  private Map<String, String> toAlertCommentMap(MatchComposite matchComposite) {
    var comments = new HashMap<String, String>();
    var matchData = matchComposite.getMatchData();

    comments.put("caseId", matchComposite.getExternalId());
    comments.put("apId", getAlertedPartyId(matchData));
    comments.put("wlId", getWatchlistId(matchData));
    comments.put("apType", getApType(matchData));
    comments.put("wlType", getWlType(matchData));
    comments.put("listName", getListName(matchData));

    return comments;
  }

  private String getWatchlistId(MatchData matchData) {
    return matchData.getWatchlistId().orElse("");
  }

  private String getAlertedPartyId(MatchData matchData) {
    return matchData.getCaseInformation().getExternalId();
  }

  private String getWlType(MatchData matchData) {
    var extendedAttribute5 = matchData.getCaseInformation().getExtendedAttribute5();

    return wlTypesMap.entrySet().stream()
        .filter(e -> e.getValue().contains(extendedAttribute5))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse("OTHER");
  }

  private String getListName(MatchData matchData) {
    return matchData.getCaseInformation().getExtendedAttribute5();
  }

  private String getApType(MatchData matchData) {
    return matchData.isIndividual() ? INDIVIDUAL : ENTITY;
  }

  private List<MatchCommentInputDto> createMatchCommentInputs(MatchComposite matchComposite) {
    return List.of(
        MatchCommentInputDto.builder()
            .match(matchComposite.getName())
            .commentInput(Map.of())
            .build());
  }
}

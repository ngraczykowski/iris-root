package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.datamodel.MatchData;

import one.util.streamex.StreamEx;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Collections.sort;

@RequiredArgsConstructor
public class GetCommentInputUseCase {

  private static final String INDIVIDUAL = "I";
  private static final String ENTITY = "C";
  private final MatchFacade matchFacade;

  @Transactional(readOnly = true)
  public List<CommentInputDto> getInputRequestsResponse(StreamCommentInputsRequestDto request) {
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

    return commentInputs;
  }

  private Map<String, String> toAlertCommentMap(MatchComposite matchComposite) {
    var comments = new HashMap<String, String>();
    var type = getType(matchComposite.getMatchData());

    comments.put("caseId", matchComposite.getExternalId());
    comments.put("apId", getAlertedPartyId(matchComposite.getMatchData()));
    comments.put("wlId", getWatchlistId(matchComposite.getMatchData()));
    comments.put("apType", type);
    comments.put("wlType", type);
    comments.put("listName", getListName(matchComposite.getMatchData()));

    return comments;
  }

  private String getWatchlistId(MatchData matchData) {
    return matchData.getWatchlistId().orElse("");
  }

  private String getAlertedPartyId(MatchData matchData) {
    return matchData.getCaseInformation().getExternalId();
  }

  private String getListName(MatchData matchData) {
    return matchData.getCaseInformation().getExtendedAttribute10();
  }

  private String getType(MatchData matchData) {
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

package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.alert.AlertInfo;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import com.google.protobuf.Struct;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class GetCommentInputUseCase {

  private final AlertFacade alertFacade;
  private final MatchFacade matchFacade;

  public List<CommentInputDto> getInputRequestsResponse(StreamCommentInputsRequestDto request) {
    var commentInputs = new ArrayList<CommentInputDto>();

    request.getAlerts().forEach(alertName -> {
      var alertIds = getAlertIds(alertName);
      alertIds.forEach(alertId -> {
        var matchCommentInputs = createMatchCommentInputs(alertId);
        commentInputs.add(createCommentInput(alertName, matchCommentInputs));
      });
    });
    return commentInputs;
  }

  private CommentInputDto createCommentInput(String alertName, List<MatchCommentInputDto> match) {
    return CommentInputDto.builder()
        .alert(alertName)
        .alertCommentInput(Struct.getDefaultInstance())
        .matchCommentInputsDto(match)
        .build();
  }

  private List<MatchCommentInputDto> createMatchCommentInputs(Long alertId) {
    return matchFacade.getMatchesByAlertId(alertId).stream()
        .map(matchComposite ->
            MatchCommentInputDto.builder()
                .match(matchComposite.getName())
                .commentInput(Struct.getDefaultInstance())
                .build())
        .collect(toList());
  }

  private List<Long> getAlertIds(String alertName) {
    return alertFacade.getAlertByName(alertName).stream()
        .map(AlertInfo::getId)
        .collect(toList());
  }
}

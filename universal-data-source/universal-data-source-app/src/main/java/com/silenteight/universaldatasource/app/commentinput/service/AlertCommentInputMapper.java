package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.datasource.comments.api.v2.MatchCommentInput;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.protobuf.Struct;
import org.springframework.stereotype.Component;
import protobuf.ProtoMessageToObjectNodeConverter;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
class AlertCommentInputMapper {

  private final ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter;

  List<AlertCommentInput> map(List<CommentInput> commentInputs) {
    return commentInputs.stream()
        .map(c -> AlertCommentInput.builder()
            .alert(c.getAlert())
            .commentInput(mapAlertCommentInput(c.getAlertCommentInput()))
            .matchCommentInputs(mapMatchCommentInput(c.getMatchCommentInputsList()))
            .build())
        .collect(Collectors.toList());
  }

  private String mapAlertCommentInput(Struct alertCommentInput) {
    return protoMessageToObjectNodeConverter
        .convert(alertCommentInput)
        .orElseThrow()
        .toString();
  }

  private String mapMatchCommentInput(List<MatchCommentInput> matchCommentInputsList) {

    var arrayNode = JsonNodeFactory.instance.arrayNode();
    for (MatchCommentInput commentInput : matchCommentInputsList) {
      var jsonNodes = protoMessageToObjectNodeConverter.convert(commentInput)
          .orElseThrow(
              () -> new AlertCommentInputMappingException(
                  "Could not map match comment input to string"));
      arrayNode.add(jsonNodes);
    }
    return arrayNode.toString();
  }

  private static class AlertCommentInputMappingException extends RuntimeException {

    private static final long serialVersionUID = -2486480300364133347L;

    AlertCommentInputMappingException(String message) {
      super(message);
    }
  }

}

package com.silenteight.universaldatasource.app.commentinput.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.datasource.comments.api.v2.MatchCommentInput;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
class CommentInputMapper {

  public CommentInput map(AlertCommentInput alertCommentInput) {

    var commentInputBuilder = CommentInput.newBuilder()
        .setName("comment-inputs/" + alertCommentInput.getCommentInputId())
        .setAlert(alertCommentInput.getAlert());

    var structBuilder = Struct.newBuilder();
    mapToStruct(structBuilder, alertCommentInput.getCommentInput());
    commentInputBuilder.setAlertCommentInput(structBuilder.build());

    var matchCommentInputList =
        getMatchCommentInputList(alertCommentInput.getMatchCommentInputs());
    commentInputBuilder.addAllMatchCommentInputs(matchCommentInputList);

    return commentInputBuilder.build();
  }

  private void mapToStruct(Struct.Builder structBuilder, String commentInput) {
    try {
      JsonFormat.parser().merge(
          commentInput,
          structBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new CommentInputJsonConversionException(
          "Cannot convert agent feature input to Struct", e);
    }
  }

  private List<MatchCommentInput> getMatchCommentInputList(String matchCommentInputs) {

    Iterator<JsonNode> matchCommentInputJsonIterator = getJsonNodeIterator(matchCommentInputs);
    List<MatchCommentInput> matchCommentInputList = new ArrayList<>();

    while (matchCommentInputJsonIterator.hasNext()) {
      var matchCommentInputJson = matchCommentInputJsonIterator.next();
      var matchCommentInputBuilder = MatchCommentInput.newBuilder();
      mapToMessage(matchCommentInputBuilder, matchCommentInputJson.toString());
      matchCommentInputList.add(matchCommentInputBuilder.build());
    }
    return matchCommentInputList;
  }

  private Iterator<JsonNode> getJsonNodeIterator(String matchCommentInputs) {
    try {
      return new ObjectMapper().readTree(matchCommentInputs).iterator();
    } catch (JsonProcessingException e) {
      throw new CommentInputJsonConversionException(
          "Cannot convert match comment input to JsonNode", e);
    }
  }

  private void mapToMessage(
      Message.Builder featureInputBuilder, String messageCommentInput) {
    try {
      JsonFormat.parser().merge(
          messageCommentInput,
          featureInputBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new CommentInputJsonConversionException(
          "Cannot convert agent feature input to Struct", e);
    }
  }

  private static class CommentInputJsonConversionException extends RuntimeException {

    private static final long serialVersionUID = -204330995574515285L;

    CommentInputJsonConversionException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}

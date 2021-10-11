package com.silenteight.universaldatasource.app.commentinput;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputRequest;
import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.datasource.comments.api.v2.MatchCommentInput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.util.JsonFormat;

import java.util.List;

class CommentInputFixture {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static BatchCreateCommentInputRequest getBatchCommentInputRequest() throws
      InvalidProtocolBufferException {

    return BatchCreateCommentInputRequest.newBuilder()
        .addAllCommentInputs(
            List.of(
                getCommentInput("1"), getCommentInput("2"),
                getCommentInputWithoutMatchCommentInput("3")))
        .build();
  }

  private static CommentInput getCommentInput(String alertId) throws
      InvalidProtocolBufferException {

    return CommentInput.newBuilder()
        .setAlert("alerts/" + alertId)
        .setAlertCommentInput(getAlertCommentInput())
        .addAllMatchCommentInputs(
            List.of(
                getMatchCommentInput(alertId, "1"),
                getMatchCommentInput(alertId, "2")))
        .build();
  }

  private static CommentInput getCommentInputWithoutMatchCommentInput(String alertId) throws
      InvalidProtocolBufferException {
    return CommentInput.newBuilder()
        .setAlert("alerts/" + alertId)
        .setAlertCommentInput(getAlertCommentInput())
        .build();
  }

  private static Struct getAlertCommentInput() throws InvalidProtocolBufferException {

    ObjectNode commentInputObjectNodeOne = MAPPER.createObjectNode();
    commentInputObjectNodeOne.set(
        "field1", (MAPPER.convertValue("AlertComment number 1", JsonNode.class)));

    Builder structBuilder = Struct.newBuilder();
    JsonFormat.parser().merge(commentInputObjectNodeOne.toString(), structBuilder);

    return structBuilder.build();
  }

  private static MatchCommentInput getMatchCommentInput(String alertId, String matchId) throws
      InvalidProtocolBufferException {

    return MatchCommentInput.newBuilder()
        .setMatch("alerts/" + alertId + "/matches/" + matchId)
        .setCommentInput(getMatchCommentInput())
        .build();
  }

  private static Struct getMatchCommentInput() throws InvalidProtocolBufferException {
    ObjectNode matchCommentInputObjectNodeOne = MAPPER.createObjectNode();
    matchCommentInputObjectNodeOne.set(
        "field1", (MAPPER.convertValue("MatchComment number 1", JsonNode.class)));
    Builder matchCommentBuilder = Struct.newBuilder();
    JsonFormat.parser().merge(matchCommentInputObjectNodeOne.toString(), matchCommentBuilder);
    return matchCommentBuilder.build();
  }
}

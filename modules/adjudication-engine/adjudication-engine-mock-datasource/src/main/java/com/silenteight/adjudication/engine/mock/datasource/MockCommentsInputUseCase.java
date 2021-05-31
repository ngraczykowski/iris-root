package com.silenteight.adjudication.engine.mock.datasource;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v1.CommentInput;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class MockCommentsInputUseCase {

  public List<CommentInput> getcommentInputs(List<String> alerts) {
    log.info("Mock getCommentInputs for:" + alerts);
    return alerts.stream().map(alert ->
        CommentInput.newBuilder()
            .setAlert(alert)
            .setAlertCommentInput(Struct.newBuilder()
                .putFields("field1", Value.newBuilder().setStringValue("test1-" + alert).build())
                .putFields("field2", Value.newBuilder().setNumberValue(2).build())
                .build())
            .build()
    ).collect(Collectors.toList());
  }
}

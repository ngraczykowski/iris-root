/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.silenteight.adjudication.engine.solving.application.process.CommentInputFixture.COMMENT_INPUT_KEY;
import static com.silenteight.adjudication.engine.solving.application.process.CommentInputFixture.COMMENT_INPUT_VALUE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentInputResolveProcessTest {

  private CommentInputResolveProcess commentInputResolveProcess;
  private CommentInputClientRepository commentInputClientRepository;

  @BeforeEach
  void setUp() {
    var commentInputClient = new MockCommentInputClient();
    var converter =
        new ProtoMessageToObjectNodeConverter(
            new MessageRegistryFactory(
                    "com.google.api",
                    "com.google.protobuf",
                    "com.google.rpc",
                    "com.google.type",
                    "com.silenteight.dataretention")
                .create());
    commentInputClientRepository = new CommentInputClientRepository(new HashMap<>(), converter);
    commentInputResolveProcess =
        new CommentInputResolveProcess(commentInputClient, converter, commentInputClientRepository);
  }

  @Test
  void shouldSaveCommentInput() {
    commentInputResolveProcess.retrieveCommentInput("alerts/1");
    var commentInput = commentInputClientRepository.get(1);
    assertThat(commentInput.get(COMMENT_INPUT_KEY)).isEqualTo(COMMENT_INPUT_VALUE);
  }
}

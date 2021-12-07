package com.silenteight.universaldatasource.api.library.commentinput.v2;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputRequest;
import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;
import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc;
import com.silenteight.datasource.comments.api.v2.CreatedCommentInput;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.Map;

class CommentInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private CommentInputGrpcAdapter underTest;


  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedCommentInputGrpcServer());

    var stub = CommentInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new CommentInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldCreateCommentInputs() {
    //when
    var response = underTest.createCommentInputs(Fixtures.REQUEST);

    Assertions.assertEquals(response.getCreatedCommentInputs().size(), 1);

    CreatedCommentInputOut first = response.getCreatedCommentInputs().stream().findFirst().get();
    Assertions.assertEquals(first.getName(), Fixtures.NAME);
    Assertions.assertEquals(first.getAlert(), Fixtures.ALERT);
  }

  static class MockedCommentInputGrpcServer
      extends CommentInputServiceGrpc.CommentInputServiceImplBase {

    @Override
    public void batchCreateCommentInput(
        BatchCreateCommentInputRequest request,
        StreamObserver<BatchCreateCommentInputResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String NAME = "name";
    public static final String ALERT = "alert";
    public static final String MATCH = "match";
    public static final Map<String, String> ALERT_COMMENT_INPUTS = Map.of("key", "value");
    public static final Map<String, String> MATCH_COMMENT_INPUTS = Map.of("key1", "value1");

    static final List<MatchCommentInputIn> MATCH_COMMENT_INPUT_INS = List.of(
        MatchCommentInputIn.builder()
            .match(MATCH)
            .commentInput(MATCH_COMMENT_INPUTS)
            .build()
    );

    static final List<CommentInputIn> COMMENT_INPUT_INS = List.of(
        CommentInputIn.builder()
            .name(NAME)
            .alert(ALERT)
            .alertCommentInputs(ALERT_COMMENT_INPUTS)
            .matchCommentInputs(MATCH_COMMENT_INPUT_INS)
            .build()
    );

    static final BatchCreateCommentInputIn REQUEST = BatchCreateCommentInputIn.builder()
        .commentInputs(COMMENT_INPUT_INS)
        .build();

    static final List<CreatedCommentInput> CREATED_COMMENT_INPUTS = List.of(
        CreatedCommentInput.newBuilder()
            .setName(NAME)
            .setAlert(ALERT)
            .build()
    );

    static final BatchCreateCommentInputResponse GRPC_RESPONSE =
        BatchCreateCommentInputResponse.newBuilder()
            .addAllCreatedCommentInputs(CREATED_COMMENT_INPUTS)
            .build();
  }
}

package com.silenteight.universaldatasource.app.commentinput;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.universaldatasource.app.UniversalDataSourceApplication;

import com.google.common.collect.Iterators;
import com.google.protobuf.InvalidProtocolBufferException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;

import static com.silenteight.universaldatasource.app.commentinput.CommentInputFixture.getBatchCommentInputRequest;
import static com.silenteight.universaldatasource.app.commentinput.CommentInputTestDataAccess.generatedCommentInputsCount;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = UniversalDataSourceApplication.class,
    properties = "debug=true")
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
@Slf4j
class CommentInputIntegrationTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  MessageRegistry messageRegistry;

  @GrpcClient("uds")
  private CommentInputServiceBlockingStub commentInputServiceBlockingStub;

  @GrpcClient("uds")
  private CommentInputServiceGrpc.CommentInputServiceBlockingStub commentInputServiceBlockingStubV1;

  @Test
  void testBatchCreateCommentInput() throws InvalidProtocolBufferException {
    var batchCreateCommentInputResponse =
        commentInputServiceBlockingStub.batchCreateCommentInput(getBatchCommentInputRequest());

    assertThat(batchCreateCommentInputResponse.getCreatedCommentInputsCount()).isEqualTo(3);

    assertThat(batchCreateCommentInputResponse.getCreatedCommentInputsList()
        .stream()
        .filter(c -> c.getAlert().equals("alerts/1"))
        .count()).isEqualTo(1);
  }

  @Test
  void testBatchGetCommentInputs() throws InvalidProtocolBufferException {
    populateDbWithCommentInputs();

    var request = BatchGetAlertsCommentInputsRequest.newBuilder()
        .addAllAlerts(List.of("alerts/1", "alerts/2", "alerts/3"))
        .build();

    var batchGetAlertsCommentInputsResponse =
        commentInputServiceBlockingStub.batchGetAlertsCommentInputs(request);

    assertThat(batchGetAlertsCommentInputsResponse.getCommentInputsCount()).isEqualTo(3);

    var commentInputsList = batchGetAlertsCommentInputsResponse.getCommentInputsList();
    assertThat(commentInputsList.stream()
        .filter(c -> c.getAlert().equals("alerts/1"))
        .filter(c -> c.getAlertCommentInput().containsFields("field1"))
        .filter(c -> c.getMatchCommentInputsCount() == 2)
        .filter(c -> c.getName().contains("comment-inputs/"))
        .count()).isEqualTo(1);
    assertThat(commentInputsList.stream()
        .filter(c -> c.getAlert().equals("alerts/2"))
        .filter(c -> c.getAlertCommentInput().containsFields("field1"))
        .filter(c -> c.getMatchCommentInputsCount() == 2)
        .filter(c -> c.getName().contains("comment-inputs/"))
        .count()).isEqualTo(1);
  }
  
  @Test
  void testStreamCommentInputsVersionOne() throws InvalidProtocolBufferException {
    populateDbWithCommentInputs();

    var request = StreamCommentInputsRequest.newBuilder()
        .addAllAlerts(List.of("alerts/1", "alerts/2"))
        .build();

    var commentInputIterator =
        commentInputServiceBlockingStubV1.streamCommentInputs(request);

    assertThat(Iterators.size(commentInputIterator)).isEqualTo(2);

  }

  private void populateDbWithCommentInputs() throws InvalidProtocolBufferException {
    if (generatedCommentInputsCount(jdbcTemplate) == 0) {
      commentInputServiceBlockingStub.batchCreateCommentInput(getBatchCommentInputRequest());
      await()
          .atMost(Duration.ofSeconds(5000))
          .until(() -> generatedCommentInputsCount(jdbcTemplate) > 0);
    }
  }
}

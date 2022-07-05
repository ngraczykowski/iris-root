package com.silenteight.agent.autoconfigure.grpc.test;

import lombok.Getter;

import com.silenteight.agent.autoconfigure.grpc.metadata.AgentInformationProvider;
import com.silenteight.agent.autoconfigure.grpc.metadata.GrpcMetadataEnhancerConfigurer;

import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;

import java.util.concurrent.atomic.AtomicReference;

import static io.grpc.stub.MetadataUtils.newCaptureMetadataInterceptor;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused") //Note: methods used in agent's grpc test
public abstract class AgentGrpcServiceTest {

  protected static final String AGENT_NAME_FIELD = "agent";
  protected static final String AGENT_VERSION_FIELD = "version";

  protected static final AgentInformationProvider AGENT_INFORMATION =
      new AgentInformationProviderMock("ThisIsNameAgent", "0.0.1-beta");

  protected final GrpcServerConfigurer metadataEnhancer = new GrpcMetadataEnhancerConfigurer(
      AGENT_INFORMATION, AGENT_NAME_FIELD, AGENT_VERSION_FIELD
  ).getGrpcServerConfigurer();

  protected final MetadataFixtures metadataFixtures = new MetadataFixtures();

  @Getter
  protected static class MetadataFixtures {

    private final AtomicReference<Metadata> headersCapture = new AtomicReference<>(new Metadata());
    private final AtomicReference<Metadata> unusedTrailerCapture =
        new AtomicReference<>(new Metadata());
    private final ClientInterceptor metadataCapturingInterceptor =
        newCaptureMetadataInterceptor(headersCapture, unusedTrailerCapture);
  }

  protected void assertMetadataIsCorrect(Metadata actual) {
    var nameKey = Key.of(AGENT_NAME_FIELD, Metadata.ASCII_STRING_MARSHALLER);
    var versionKey = Key.of(AGENT_VERSION_FIELD, Metadata.ASCII_STRING_MARSHALLER);
    assertThat(actual.get(nameKey)).isEqualTo(AGENT_INFORMATION.getName());
    assertThat(actual.get(versionKey)).isEqualTo(AGENT_INFORMATION.getVersion());
  }
}

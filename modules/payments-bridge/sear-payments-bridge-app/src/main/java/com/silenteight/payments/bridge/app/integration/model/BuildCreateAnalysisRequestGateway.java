package com.silenteight.payments.bridge.app.integration.model;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import static com.silenteight.payments.bridge.app.integration.model.BuildCreateAnalysisRequestReplyEndpoint.BUILD_CREATE_ANALYSIS_CHANNEL;

@MessagingGateway
@SuppressWarnings("unused")
interface BuildCreateAnalysisRequestGateway extends BuildCreateAnalysisRequestPort {

  @Gateway(requestChannel = BUILD_CREATE_ANALYSIS_CHANNEL)
  @Payload("new java.util.Date()")
  CreateAnalysisRequest buildFromCurrentModel();

}

package com.silenteight.serp.governance.mailer;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.notifier.v1.SendMailCommand;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MailNotificationIntegrationProperties.class)
class MailNotificationIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final SendMailCommandHandler sendMailCommandHandler;
  private final MailNotificationIntegrationProperties properties;

  @Bean
  @ConditionalOnProperty("spring.mail.host")
  IntegrationFlow receiveSendMailCommandFlow(JavaMailSender mailSender) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(properties.getInboundQueueName())))
        .handle(
            SendMailCommand.class,
            (p, h) -> sendMailCommandHandler.handle(p).orElse(null))
        .handle(Mail.outboundAdapter(mailSender))
        .get();
  }
}

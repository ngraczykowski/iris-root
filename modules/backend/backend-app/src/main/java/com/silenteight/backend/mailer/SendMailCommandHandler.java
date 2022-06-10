/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.mailer;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.notifier.v1.SendMailCommand;

import org.springframework.mail.MailMessage;

import java.util.Optional;

@RequiredArgsConstructor
class SendMailCommandHandler {

  private final AddressedMailMessageCreator messageCreator;

  public Optional<? extends MailMessage> handle(SendMailCommand command) {
    return messageCreator.create(m -> {
      m.setSubject(command.getSubject());
      m.setText(command.getBody());
    });
  }
}

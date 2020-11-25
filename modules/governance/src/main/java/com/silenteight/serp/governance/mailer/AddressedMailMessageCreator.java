package com.silenteight.serp.governance.mailer;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
class AddressedMailMessageCreator {

  private final String from;
  private final List<String> toAddresses;

  Optional<SimpleMailMessage> create(Consumer<SimpleMailMessage> configurer) {
    if (StringUtils.isBlank(from) || CollectionUtils.isEmpty(toAddresses))
      return empty();

    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(from);
    message.setTo(toAddresses.toArray(new String[0]));

    configurer.accept(message);

    return of(message);
  }
}

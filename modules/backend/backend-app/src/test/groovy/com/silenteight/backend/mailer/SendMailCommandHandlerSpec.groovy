/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.mailer

import com.silenteight.proto.serp.notifier.v1.SendMailCommand
import com.silenteight.backend.mailer.AddressedMailMessageCreator
import com.silenteight.backend.mailer.SendMailCommandHandler

import org.springframework.mail.SimpleMailMessage
import spock.lang.Specification

import java.util.function.Consumer

class SendMailCommandHandlerSpec extends Specification {

  def addressedMailMessageCreator = Mock(AddressedMailMessageCreator)
  def underTest = new SendMailCommandHandler(addressedMailMessageCreator)

  def someMailMessageResponse = Optional.of(new SimpleMailMessage())

  def 'should handle and create message'() {
    given:
    def sendMailCommand = SendMailCommand.newBuilder().build()

    when:
    def result = underTest.handle(sendMailCommand)

    then:
    1 * addressedMailMessageCreator.create(_ as Consumer) >> someMailMessageResponse
    result == someMailMessageResponse
  }
}

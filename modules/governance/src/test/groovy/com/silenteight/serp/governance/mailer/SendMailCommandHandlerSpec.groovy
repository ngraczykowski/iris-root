package com.silenteight.serp.governance.mailer

import com.silenteight.proto.serp.notifier.v1.SendMailCommand

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

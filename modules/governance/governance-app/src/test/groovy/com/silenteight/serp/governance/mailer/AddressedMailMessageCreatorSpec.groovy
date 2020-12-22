package com.silenteight.serp.governance.mailer

import org.springframework.mail.SimpleMailMessage
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Consumer

class AddressedMailMessageCreatorSpec extends Specification {

  def someConsumer = Mock(Consumer)

  def 'should create mail message'() {
    given:
    def from = 'from'
    def toAddresses = ['to']

    when:
    def result = new AddressedMailMessageCreator(from, toAddresses).create(someConsumer)

    then:
    result.isPresent()
    with(result.get()) {
      from == from
      toAddresses == toAddresses
    }
    1 * someConsumer.accept(_ as SimpleMailMessage)
  }

  @Unroll
  def 'should do not create mail message, from = #from, toAddresses = #toAddresses'() {
    when:
    def result = new AddressedMailMessageCreator(from, toAddresses).create(someConsumer)

    then:
    !result.isPresent()
    0 * someConsumer.accept(_ as SimpleMailMessage)

    where:
    from   | toAddresses
    null   | null
    ''     | null
    null   | []
    ''     | []
    'from' | null
    'from' | []
  }
}

package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.infrastructure.parser.FileParserConfiguration
import com.silenteight.qco.infrastructure.parser.SolutionConfigurationFileProperties

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import spock.lang.Specification

class SimpleMatchChangeConditionFactorySpec extends Specification {

  def resourceLoader = Mock(ResourceLoader)
  def properties = new SolutionConfigurationFileProperties("config/qco_config_file.csv", (char) '|')


  def "The specified policy,step and solution should be found in the configuration map"() {
    given:
    def policyId = 'policies/c80a65e9-0b79-46d4-8e40-54065ae14e7b'
    def stepId = 'steps/6a973b62-ef1e-4e88-b756-a7a31f1cd06c'
    def solution = 'SOLUTION_FALSE_POSITIVE'
    resourceLoader.getResource(_) >> new ClassPathResource(properties.location())
    def fileParserConfiguration = new FileParserConfiguration(resourceLoader, properties)
    def csvStreamParser = fileParserConfiguration.csvStreamParser()
    def csvProvider = fileParserConfiguration.solutionConfigurationCsvFileParser(csvStreamParser)
    def configurationHolder = new QcoConfigurationHolder(csvProvider)
    configurationHolder.init()
    def underTest = new SimpleMatchChangeConditionFactory(configurationHolder)

    when:
    def condition = underTest.createChangeCondition(policyId, stepId, solution)

    then:
    condition == new ChangeCondition(policyId, stepId, solution)
  }

  def "The specified policy,step and solution shouldn't be found in the configuration map"() {
    given:
    def policyId = 'policies/c80a65e9-0b79-46d4-8e40-54065ae14e7q'
    def stepId = 'steps/6a973b62-ef1e-4e88-b756-a7a31f1cd06c'
    def solution = 'SOLUTION_FALSE_POSITIVE'
    resourceLoader.getResource(_) >> new ClassPathResource(properties.location())
    def fileParserConfiguration = new FileParserConfiguration(resourceLoader, properties)
    def csvStreamParser = fileParserConfiguration.csvStreamParser()
    def csvProvider = fileParserConfiguration.solutionConfigurationCsvFileParser(csvStreamParser)
    def configurationHolder = new QcoConfigurationHolder(csvProvider)
    configurationHolder.init()
    def underTest = new SimpleMatchChangeConditionFactory(configurationHolder)

    when:
    def condition = underTest.createChangeCondition(policyId, stepId, solution)

    then:
    condition != new ChangeCondition(policyId, stepId, solution)
    condition == ChangeCondition.NO_CONDITION_FULFILLED
  }
}

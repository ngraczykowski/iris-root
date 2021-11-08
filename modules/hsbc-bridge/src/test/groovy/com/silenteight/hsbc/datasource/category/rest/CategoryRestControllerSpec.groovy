package com.silenteight.hsbc.datasource.category.rest

import com.silenteight.hsbc.datasource.category.CategoryType
import com.silenteight.hsbc.datasource.category.ListCategoriesUseCase
import com.silenteight.hsbc.datasource.category.dto.CategoryDto

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class CategoryRestControllerSpec extends Specification {

  def listCategoriesUseCase = Mock(ListCategoriesUseCase)
  def controller = new CategoryRestController(listCategoriesUseCase)
  MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
  def categories = createCategories()
  def emptyCategories = []

  def 'should return categories'() {
    when:
    def results = mockMvc.perform(MockMvcRequestBuilders.get('/categories'))

    then:
    1 * listCategoriesUseCase.getCategories() >> categories
    verifyResults(results)
  }

  def 'should return empty categories'() {
    when:
    def results = mockMvc.perform(MockMvcRequestBuilders.get('/categories'))

    then:
    1 * listCategoriesUseCase.getCategories() >> emptyCategories
    verifyEmptyResults(results)
  }

  static def verifyResults(ResultActions results) {
    results.andExpect(MockMvcResultMatchers.status().isOk())
    results.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(
        MockMvcResultMatchers.content().string(
            '[{"name":"hitType","displayName":"Hit Type (PEP / AM / SAN)","categoryType":"ENUMERATED","allowedValues":["SAN","PEP","AM"],"multiValue":false}]'))
  }

  static def createCategories() {
    [
        CategoryDto.builder()
            .name('hitType')
            .displayName('Hit Type (PEP / AM / SAN)')
            .categoryType(CategoryType.ENUMERATED)
            .allowedValues(List.of('SAN', 'PEP', 'AM'))
            .multiValue(false)
            .build()
    ]
  }

  static def verifyEmptyResults(ResultActions results) {
    results.andExpect(MockMvcResultMatchers.status().isOk())
    results.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(MockMvcResultMatchers.content().string('[]'))
  }
}

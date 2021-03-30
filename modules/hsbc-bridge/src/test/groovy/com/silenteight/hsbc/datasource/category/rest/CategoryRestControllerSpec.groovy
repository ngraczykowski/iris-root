package com.silenteight.hsbc.datasource.category.rest

import com.silenteight.hsbc.datasource.category.ListCategoriesUseCase
import com.silenteight.hsbc.datasource.category.dto.CategoryDto

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static com.silenteight.hsbc.datasource.category.CategoryType.ENUMERATED
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class CategoryRestControllerSpec extends Specification {

  def listCategoriesUseCase = Mock(ListCategoriesUseCase)
  def controller = new CategoryRestController(listCategoriesUseCase)
  MockMvc mockMvc = standaloneSetup(controller).build()
  def categories = createCategories()
  def emptyCategories = []

  def 'should return categories'() {
    when:
    def results = mockMvc.perform(get('/category'))

    then:
    1 * listCategoriesUseCase.getCategories() >> categories
    verifyResults(results)
  }

  def 'should return empty categories'() {
    when:
    def results = mockMvc.perform(get('/category'))

    then:
    1 * listCategoriesUseCase.getCategories() >> emptyCategories
    verifyEmptyResults(results)
  }

  def verifyResults(ResultActions results) {
    results.andExpect(status().isOk())
    results.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(content().string('[{"name":"hitType","displayName":"Hit Type (PEP / AM / SAN)","categoryType":"ENUMERATED","allowedValues":["SAN","PEP","AM"],"multiValue":false}]'))
  }

  def createCategories() {
    [
        CategoryDto.builder()
            .name('hitType')
            .displayName('Hit Type (PEP / AM / SAN)')
            .categoryType(ENUMERATED)
            .allowedValues(List.of('SAN', 'PEP', 'AM'))
            .multiValue(false)
            .build()
    ]
  }

  def verifyEmptyResults(ResultActions results) {
    results.andExpect(status().isOk())
    results.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(content().string('[]'))
  }
}

#TODO(dsniezek): temporary move after gov, sim, webapp merged to one backend application
#into smoke tests for all env
@hotel-backend
Feature: Hotel Backend scenarios

  Background:
    Given Default user is admin

  Scenario: Frontend Configuration via API is accessible without login
    Given Frontend Configuration API respond with 200 status code

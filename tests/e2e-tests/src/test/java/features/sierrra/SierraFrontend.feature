@sierra
Feature: Sierra Frontend scenarios

  Scenario: Create a simple policy with steps via frontend
    Given User login as admin
    And User is on main page
    When User clicks on button with text "Policies" on side navigation
    And User is on policies page
    And User clicks on new policy button
    And User fills policy name input with "Something hello"
    And User fills policy description input with "Lorem ipsum dolor sit amet"
    And User clicks on steps tab button
    And User clicks on add new step button
    And User fills step name input with "Step name"
    And User fills step description input with "Lorem ipsum dolor sit amet"
    And User set s8 solution select as "False Positive"
    And User clicks on add new rule button
    And User set feature name select as "Name"
    And User set feature condition select as "is"
    And User set feature value select as "No Match"
    And User clicks on mark as ready button
    And User clicks on mark as ready confirmation button
    Then User sees success prompt with text "Policy Unnamed Policy successfully marked as ready"

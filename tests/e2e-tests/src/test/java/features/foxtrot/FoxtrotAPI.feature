@foxtrot
Feature: Foxtrot API scenarios

  Background:
    Then Create user "A" with random name
    And Assign user "A" to roles
      | MODEL_TUNER |
    And Create user "B" with random name
    And Assign user "B" to roles
      | APPROVER |
    And Default user is "A"

  Scenario: Send alert to FTCC
    Given Send 5 alerts on "learning" to FTCC.
    And Create empty policy with name "QA Policy for simulation test"
    And Add steps to recently created policy
      | stepInternalId  | name      | solution                |
      | 1               | some step | POTENTIAL_TRUE_POSITIVE |
    And Add features to recently created steps
      | stepInternalId  |  name             | condition | values |
      | 1               | features/gender   | is        | YES    |
    And Mark created policy as ready
    And Create dataset with name "QA Dataset for simulation test" for recently created learning
    And Create solving model for created policy
    When Create simulation based on created policy and dataset with name "QA Simulation"
    And Wait until simulation is done
    And Create policy state change request
    Then Activate solving model as user "B"

  Scenario: Solving via FTCC
    Given Send 5 alerts on "solving" to FTCC.
    And Give it time - 5 seconds
    And Create country group
    And Add countries to country group
      | PL |
      | MX |
      | SG |
    And Assign user "A" to country group
    And Prepare report date range for today
    When Initialize generation of "AI_REASONING" report via warehouse and wait until it's generated
    And Download generated report
    Then Downloaded report contains 5 rows

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
    And User set feature name select as "Gender"
    And User set feature condition select as "is"
    And User set feature value select as "No Match"
    And User clicks on mark as ready button
    And User clicks on mark as ready confirmation button
    Then User sees success prompt with text "Policy Unnamed Policy successfully marked as ready"

  Scenario: Create a simple dataset and then run a simulation against it via frontend
    Given User login as admin
    And User is on main page
    And User clicks on button with text "Datasets" on side navigation
    And User is on datasets page
    And User clicks on add dataset button
    And User fills dataset name with "Dataset name"
    And User set date range as today
    And User clicks on save dataset button
    When User clicks on button with text "Simulations" on side navigation
    And User is on simulations page
    And User clicks on create simulation button
    And User fills simulation desc with "Simulation name"
    And User fills simulation desc with "Lorem ipsum dolor sit amet"
    And User set policy id as "Unnamed Policy"
    And User set dataset as "Dataset name"
    And User clicks on run simulation button
    Then Simulation with name "Simulation name" is displayed on list

@foxtrot
Feature: Foxtrot frontend scenarios

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
And User fills simulation name with "Simulation name"
And User fills simulation desc with "Lorem ipsum dolor sit amet"
And User set policy id as "Unnamed Policy"
And User set dataset as "Dataset name"
And User clicks on run simulation button
Then Simulation with name "Simulation name" is displayed on list

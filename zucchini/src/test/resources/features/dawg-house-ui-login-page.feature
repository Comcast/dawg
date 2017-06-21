@uitest @uitest1
Feature: Log In
	Verify the log in behaviour of the dawg house portal

Background:
	Given I am on the Log In page of dawg house portal

@uitest11
Scenario: Login in to dawg house with valid credentials
	When I enter correct username and password
	And I select 'Sign In' button
	Then I should see the Dawg House home page

@uitest12
Scenario: Login in to dawg house with invalid credentials
	When I enter incorrect credentials
	And  I select 'Sign In' button
	Then I should see the error message "Invalid credentials"
	
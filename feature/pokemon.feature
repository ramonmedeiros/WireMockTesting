Feature: Test request to API and mock

Scenario: Testing directly API
	Given i have a running API server
	When i request /pokemon
	Then bulbasaur will be the first
	
Scenario: Testing directly to Mock
	Given i have a running mock server
	When i request /pokemon
	Then bulbasaur will be the first
		
@ignore
Feature: Downstream dependency example

  Background:
    * url downstreamApiUrlBase

    # Don't run this feature if we're not running the mock server. This band api doesn't actually exist.
    * if (env != 'mock') karate.abort()

  Scenario: get all the bands
    Given path 'v1', 'bands'
    When method get
    Then status 200
    * print response

  Scenario: create a new band
    # Create a band
    Given path 'v1', 'bands'
    And header Content-Type = 'application/json; charset=utf-8'
    And request { name: "Daft Punk", content: "Faster, Harder, Stronger." }
    When method post
    Then status 200
    * print response
    And match response['content'] == "Faster, Harder, Stronger."
    * def id = response['id']

    # Make sure the created band exists with the same content
    Given path 'v1', 'bands'
    And path id
    When method get
    Then status 200
    * print response
    And def band = response
    And match band['content'] == "Faster, Harder, Stronger."

    # Update the band with new content
    Given path 'v1', 'bands'
    And path id
    And request { name: "LP", content: "Lost on You" }
    When method put
    Then status 200
    * print response
    And def band = response
    And match band['content'] == "Lost on You"

    # Delete the created band
    Given path 'v1', 'bands'
    And path id
    When method delete
    Then status 200

    # Check for the deleted band
    Given path 'v1', 'bands'
    And path id
    * print response
    When method get
    Then status 200
    And match response !contains { id: '#(id)', name: '#string', content: '#string' }
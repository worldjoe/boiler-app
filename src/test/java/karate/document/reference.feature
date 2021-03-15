Feature: Reference Document Endpoint

    Background:
    * url baseUrl

    Scenario: get all documents
        Given path "/v1/documents"
        When method get
        Then status 200

    Scenario: create a new document
        # Create a document
        Given path "/v1/documents"
        And header Content-Type = 'application/json; charset=utf-8'
        And request { "userName":"worldjoe", "content":"This is a test document" }
        When method post
        Then status 200
        And match response['content'] == 'This is a test document'
    Scenario: access health endpoint
        Given url baseUrl+"/health/full"
        When method get
        Then status 200
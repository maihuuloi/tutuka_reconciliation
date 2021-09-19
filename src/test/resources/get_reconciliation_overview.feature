Feature: Transaction Reconciliation

  Scenario: Reconciliation with multiple matching type record
    Given two transaction files with matching, unmatched, and suggested records
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives reconciliation with result summary
    And the client receives at least 1 matching record
    And the client receives at least 1 unmatched record
    And the client receives at least 1 suggested record

  Scenario: Reconciliation with duplicate value in index column
    Given two identical transaction files with duplicate value in TransactionID column
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives 0 unmatched records
    And the client receives at least 1 matching record

  Scenario: Reconciliation with unmatched index records on two source
    Given file one contain transaction id not match in file two
    When client call reconciliation api to reconcile these transaction files
    And the client receives at least 1 matching record

  Scenario: Reconciliation with closed match records
    Given file one contain one record has only TransactionAmount matched with file two
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives 1 suggested records
    And the client receives 0 unmatched records
    And the client receives 0 matching records

  Scenario: Reconciliation with invalid format files
    Given two file with invalid CSV file
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 400

  Scenario: Reconciliation failed on empty index column
    Given two identical file with records having TransactionID empty
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives at least 1 matching record

  Scenario: Reconciliation success with missing data in criteria column
    Given two identical file with records have TransactionDate empty
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives at least 1 matching record

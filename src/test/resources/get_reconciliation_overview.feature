Feature: Transaction Reconciliation
  Scenario: Reconciliation transaction files with valid transaction records
    Given two transaction files with valid records
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And the client receives reconciliation with result summary

  Scenario: Reconcile with duplicate value in index column
    Given two identical transaction files with duplicate value in transaction id column
    When client call reconciliation api to reconcile these transaction files
    Then the client receives status code of 200
    And return valid reconciliation overview with equal metrics

  Scenario: Reconcile
    Given file one contain transaction id not match in file two
    When client call reconciliation api to reconcile these transaction files
    Then return valid number of un matched in file one greater file two

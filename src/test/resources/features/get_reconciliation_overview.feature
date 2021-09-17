Feature: Transaction Reconciliation
  Scenario: Get reconciliation from 2 valid transaction files
    Given two valid transaction files set
    When client call reconciliation api to reconcile these transaction files
    Then return valid reconciliation overview

  Scenario: Reconcile different
    Given two identical transaction files with duplicate id record set
    When client call reconciliation api to reconcile these transaction files
    Then return valid reconciliation overview with equal metrics

  Scenario: Reconcile
    Given file one contain transaction id not match in file two
    When client call reconciliation api to reconcile these transaction files
    Then return valid number of un matched in file one greater file two

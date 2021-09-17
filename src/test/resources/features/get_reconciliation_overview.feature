Feature: Transaction Reconciliation
  Scenario: Get reconciliation from 2 valid transaction files
    Given two valid transaction files set
    When client call reconciliation api to reconcile these transaction files
    Then return valid reconciliation overview

  Scenario: Reconcile different
    Given two transaction files with duplicate id record set
    When client call reconciliation api to reconcile these transaction files
    Then return valid reconciliation overview with equal metrics

  Scenario: Reconcile
    Given two identical transaction files with duplicate id record set
    When client call reconciliation api to reconcile these transaction files
    Then return valid reconciliation overview with equal metrics

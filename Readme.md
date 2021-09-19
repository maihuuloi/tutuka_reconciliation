# Business
## General
 - Reconciliation is a process to find matching records between 2 sources with a matching strategy to see the differences between
 record in different system. in the end of process, the output will be a list of matching records, unmatched record. 
 Sometimes the process can also produce suggested records which can be used for manually matching. 
 - This project is focusing on transaction reconciliation, however the implementation is generic enough for other type of reconciliation 
 as long as it follows the configuration provided
 - Currently, this project only supports one to one matching process
## Terminologies
 - *Pass rule*: a rule which is used to check against records to see if they are matched. It contains a list of *matching criteria* base on column names.
 - *Matching Criteria*: a configuration to for comparing value of a column in a record against the other record's column.
 - *Matching percentage*: to indicate how many percent the 2 records matched, this number compute base on a pass rule
## Process flow
 1. Parse 2 source file into records
 2. Use strategy for finding matching, unmatched, and suggested records. 
    2.1 Currently, 2 strategy are available:
    - Index column strategy: provide a column name to be indexed, an index (multi value map) of the second source is created for searching. 
    Walk through each record in the first source, use index to find and compute best matching record in second source, 
    the matching record will be removed from the second source list.
    - Exhaustion strategy: walk through each record in first source and find matching record in the second source,
    the matching record will be removed from the second source list.
    2.2 empty is considered as a value, that means if both record have empty on a field, they are considered as matched on that field
    
 3. Return list of matching result. Each of matching result will contain 2 record of two source and a matching percentage
    - 100% is perfect match
    - 0% is unmatched
    - in between 0 and 100 is suggested
# Technical
## Technologies
- SpringBoot
- JUnit for TDD
- Cucumber for BDD
- Lombok for code auto generation
- OpenCsv for file process
## Project structure
- This project follow SpringBoot standard project structure
- `reconciliation` is an independent submodule exposing API for processing reconciliation.
It was implemented to have boundary in itself from the parent module, this helps to reduce migration effort to a library later
- ![reconciliation module model](reconcile_model.png)

## Configuration
- To configure matching pass rule and index, the `reconciliation` module provides Interface for creating object for these configurations, 
see `com.tutuka.txmanagement.configuration.ReconciliationProviderConfiguration` for example.

## Build and Run
 1. Run `mvn clean package` to build project
 2. Run `mvn spring-boot:run` to start
## Improvement
- The matching, unmatched, and suggested rule can be provided as a configuration to ReconciliationProvider,
 and the result returned from ReconciliationProvider can have a field to indicate which matching type the result is
- File content format is not strictly checked at the moment
- Add more matching strategies 

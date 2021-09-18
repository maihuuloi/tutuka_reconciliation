package com.tutuka.txmanagement.reconciliation;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reconcile 2 set of data sources
 * @implNote This is better to become an interface, but YAGNI.
 */
public class ReconciliationProvider {

    private ReconciliationStrategy strategy;
    private final FileParser fileParser;

    public ReconciliationProvider(ReconciliationStrategy strategy, FileParser fileParser) {
        this.strategy = strategy;
        this.fileParser = fileParser;
    }

    /**
     * Parse two file and reconcile each line from one source to each line in the other source
     * to find the highest matching
     * @param source1 source input for reconcile
     * @param source2 source input for reconcile
     * @return A list of matching record result
     * @throws InvalidFileException when the provided data sources have invalid format content
     */
    public List<ReconciliationResult> reconcile(File source1, File source2) throws InvalidFileException {
        List<Record> source1Records;
        List<Record> source2Records;
        try {
            source1Records = fileParser.parse(source1);
            source2Records = fileParser.parse(source2);
        } catch (CsvException e) {
            throw new InvalidFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ReconciliationResult> reconciliationResults = strategy.reconcile(source1Records, source2Records);

        return reconciliationResults;
    }


    public static ReconciliationProviderBuilder builder() {
        return new ReconciliationProviderBuilder();
    }
}

package com.tutuka.reconciliation.provider;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.dto.TransactionRecitationResult;
import com.tutuka.reconciliation.provider.exception.InvalidFileException;
import com.tutuka.reconciliation.provider.matcher.RecordMatcher;
import com.tutuka.reconciliation.provider.model.Record;
import com.tutuka.reconciliation.provider.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.List;


public abstract class
ReconciliationProvider {
    protected RecordMatcher recordMatcher;
    private FileParser fileParser;

    public ReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser) {
        this.recordMatcher = recordMatcher;
        this.fileParser = fileParser;
    }

    public List<TransactionRecitationResult> reconcile(File source1, File source2) throws InvalidFileException {
        List<Record> source1Records = null;
        List<Record> source2Records = null;
        try {
            source1Records = fileParser.parse(source1);
            source2Records = fileParser.parse(source2);

        } catch (CsvException e) {
            throw new InvalidFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<TransactionRecitationResult> recitationResults = reconcile(source1Records, source2Records);

        return recitationResults;
    }

    protected abstract List<TransactionRecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records);
}

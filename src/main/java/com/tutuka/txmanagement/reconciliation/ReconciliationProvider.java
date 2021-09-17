package com.tutuka.txmanagement.reconciliation;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.reconciliation.matcher.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.List;


public abstract class ReconciliationProvider {
    protected RecordMatcher recordMatcher;
    private FileParser fileParser;

    public ReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser) {
        this.recordMatcher = recordMatcher;
        this.fileParser = fileParser;
    }

    public List<RecitationResult> reconcile(File source1, File source2) throws InvalidFileException {
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

        List<RecitationResult> recitationResults = reconcile(source1Records, source2Records);

        return recitationResults;
    }

    protected abstract List<RecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records);
}

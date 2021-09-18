package com.tutuka.txmanagement.controller;

import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
import com.tutuka.txmanagement.exception.BadRequestException;
import com.tutuka.txmanagement.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private ReconciliationService reconciliationService;

    @PostMapping("/reconcile")
    public ReconciliationResultResponse getConciliationOverview(@RequestParam("files") MultipartFile[] files) throws IOException {
        if (files.length != 2) {
            throw new BadRequestException("transaction.reconciliation.invalid-file-numbers", "Invalid number of files");
        }

        Path file1 = Files.createTempFile("file1", ".csv");
        Path file2 = Files.createTempFile("file2", ".csv");
        files[0].transferTo(file1);
        files[1].transferTo(file2);
        return reconciliationService.reconcile(file1.toFile(), file2.toFile());
    }
}

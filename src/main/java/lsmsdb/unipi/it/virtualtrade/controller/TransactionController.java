package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.dto.TransactionResponseDTO;
import lsmsdb.unipi.it.virtualtrade.service.TransactionService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transactions")
@RequiredArgsConstructor // Automatically generates the constructor for transactionService
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Returns the 10 most recent transactions embedded directly in the MongoPortfolio.
     * Lightning fast, optimized for the main dashboard.
     */
    @GetMapping("/{userId}/recent")
    public ResponseEntity<List<TransactionResponseDTO>> getRecentTransactions(
            @PathVariable("userId") String userId) {

        return ResponseEntity.ok(transactionService.getRecentTransactions(userId));
    }

    /**
     * Returns the full transaction history using the Linked IDs (Subset Pattern).
     * Uses pagination defaults to prevent HTTP 400 errors if the frontend omits them.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionHistory(
            @PathVariable("userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(transactionService.getTransactionHistory(userId, page, size));
    }

    /**
     * Returns paginated transactions filtered by symbol, searching only within Linked IDs.
     */
    @GetMapping("/{userId}/symbol/{symbol}")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsBySymbol(
            @PathVariable("userId") String userId,
            @PathVariable("symbol") String symbol,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(
                transactionService.getTransactionsBySymbol(userId, symbol, page, size)
        );
    }
}
package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.TransactionResponseDTO;
import lsmsdb.unipi.it.virtualtrade.service.TransactionService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Returns recent transactions embedded in MongoPortfolio.
     */
    @GetMapping("/{userId}/recent")
    public ResponseEntity<List<TransactionResponseDTO>> getRecentTransactions(
            @PathVariable("userId") String userId) {

        return ResponseEntity.ok(
                transactionService.getRecentTransactions(userId)
        );
    }

    /**
     * Returns full transaction history with pagination.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionHistory(
            @PathVariable("userId") String userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        return ResponseEntity.ok(
                transactionService.getTransactionHistory(userId, page, size)
        );
    }

    /**
     * Returns paginated transactions filtered by symbol.
     */
    @GetMapping("/{userId}/symbol/{symbol}")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsBySymbol(
            @PathVariable("userId") String userId,
            @PathVariable("symbol") String symbol,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        return ResponseEntity.ok(
                transactionService.getTransactionsBySymbol(
                userId,
                symbol,
                page,
                size
                )
        );
    }
}


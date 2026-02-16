package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.TransactionResponseDTO;
import lsmsdb.unipi.it.virtualtrade.service.TransactionService;

import org.springframework.data.domain.Page;
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
    @GetMapping("/{portfolioId}/recent")
    public List<TransactionResponseDTO> getRecentTransactions(
            @PathVariable String portfolioId) {

        return transactionService.getRecentTransactions(portfolioId);
    }

    /**
     * Returns full transaction history with pagination.
     */
    @GetMapping("/{portfolioId}")
    public Page<TransactionResponseDTO> getTransactionHistory(
            @PathVariable String portfolioId,
            @RequestParam int page,
            @RequestParam int size) {

        return transactionService.getTransactionHistory(portfolioId, page, size);
    }

    /**
     * Returns paginated transactions filtered by symbol.
     */
    @GetMapping("/{portfolioId}/symbol/{symbol}")
    public Page<TransactionResponseDTO> getTransactionsBySymbol(
            @PathVariable String portfolioId,
            @PathVariable String symbol,
            @RequestParam int page,
            @RequestParam int size) {

        return transactionService.getTransactionsBySymbol(
                portfolioId,
                symbol,
                page,
                size
        );
    }
}


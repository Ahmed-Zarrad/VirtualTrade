package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.dto.PortfolioViewDTO;
import lsmsdb.unipi.it.virtualtrade.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor // Automatically generates the constructor for portfolioService
public class PortfolioController {

    private final PortfolioService portfolioService;

    /**
     * Returns the portfolio view for a given user.
     * If the portfolio does not exist, it is created automatically in both databases.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<PortfolioViewDTO> getPortfolio(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(portfolioService.getPortfolioView(userId));
    }
}
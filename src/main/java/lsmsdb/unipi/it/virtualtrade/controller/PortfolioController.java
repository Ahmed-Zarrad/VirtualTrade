package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.PortfolioViewDTO;
import lsmsdb.unipi.it.virtualtrade.service.PortfolioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Returns the portfolio view for a given user.
     * If portfolio does not exist, it is created automatically.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<PortfolioViewDTO> getPortfolio(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(portfolioService.getPortfolioView(userId));
    }
}


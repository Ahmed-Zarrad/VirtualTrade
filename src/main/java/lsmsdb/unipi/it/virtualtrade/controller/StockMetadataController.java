package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.StockMetadataDTO;
import lsmsdb.unipi.it.virtualtrade.service.StockMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockMetadataController {

    private final StockMetadataService metadataService;


     //GET /api/metadata/AAPL

    @GetMapping("/{symbol}")
    public ResponseEntity<StockMetadataDTO> getMetadata(@PathVariable String symbol) {
        return ResponseEntity.ok(metadataService.getMetadata(symbol));
    }


     // GET /api/metadata

    @GetMapping
    public ResponseEntity<List<StockMetadataDTO>> getAllMetadata() {
        return ResponseEntity.ok(metadataService.getAllMetadata());
    }


     // GET /api/metadata/sector/Technology

    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<StockMetadataDTO>> getBySector(@PathVariable String sector) {
        return ResponseEntity.ok(metadataService.getStocksBySector(sector));
    }


     // POST /api/metadata

    @PostMapping
    public ResponseEntity<StockMetadataDTO> saveMetadata(@RequestBody StockMetadataDTO dto) {
        return ResponseEntity.ok(metadataService.saveMetadata(dto));
    }
}
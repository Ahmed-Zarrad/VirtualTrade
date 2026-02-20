package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.dto.StockMetadataDTO;
import lsmsdb.unipi.it.virtualtrade.model.StockMetadata;
import lsmsdb.unipi.it.virtualtrade.repository.StockMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMetadataService {

    private final StockMetadataRepository metadataRepository;


    public StockMetadataDTO getMetadata(String symbol) {
        StockMetadata metadata = metadataRepository.findById(symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + symbol));
        return mapToDTO(metadata);
    }


    public List<StockMetadataDTO> getStocksBySector(String sector) {
        return metadataRepository.findBySector(sector).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public StockMetadataDTO saveMetadata(StockMetadataDTO dto) {
        StockMetadata metadata = metadataRepository.findById(dto.getSymbol())
                .orElse(new StockMetadata());


        metadata.setSymbol(dto.getSymbol());
        metadata.setCompanyName(dto.getCompanyName());
        metadata.setSector(dto.getSector());
        metadata.setIndustry(dto.getIndustry());
        metadata.setDescription(dto.getDescription());
        metadata.setMarketCap(dto.getMarketCap());
        metadata.setDividendYield(dto.getDividendYield());
        metadata.setLastUpdated(Instant.now());

        return mapToDTO(metadataRepository.save(metadata));
    }


    private StockMetadataDTO mapToDTO(StockMetadata entity) {
        return StockMetadataDTO.builder()
                .symbol(entity.getSymbol())
                .companyName(entity.getCompanyName())
                .sector(entity.getSector())
                .industry(entity.getIndustry())
                .description(entity.getDescription())
                .marketCap(entity.getMarketCap())
                .dividendYield(entity.getDividendYield())
                .build();
    }
    public List<StockMetadataDTO> getAllMetadata() {
        return metadataRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
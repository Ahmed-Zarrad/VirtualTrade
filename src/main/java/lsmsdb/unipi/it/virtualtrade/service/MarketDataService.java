package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.dto.MarketDataBucketDTO;
import lsmsdb.unipi.it.virtualtrade.model.MarketDataBucket;
import lsmsdb.unipi.it.virtualtrade.repository.MarketDataBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataBucketRepository marketDataRepository;


    public MarketDataBucketDTO getIntradayData(String symbol, LocalDate date) {

        String bucketId = symbol + "_" + date.toString();


        MarketDataBucket bucket = marketDataRepository.findById(bucketId)
                .orElse(MarketDataBucket.builder()
                        .id(bucketId)
                        .symbol(symbol)
                        .date(date)
                        .ticks(Collections.emptyList())
                        .build());

        return mapToDTO(bucket);
    }

    private MarketDataBucketDTO mapToDTO(MarketDataBucket entity) {
        if (entity.getTicks() == null) {
            entity.setTicks(Collections.emptyList());
        }

        List<MarketDataBucketDTO.PriceTickDTO> tickDTOs = entity.getTicks().stream()
                .map(t -> new MarketDataBucketDTO.PriceTickDTO(
                        t.getM(), t.getO(), t.getH(), t.getL(), t.getC(), t.getV()))
                .collect(Collectors.toList());

        return MarketDataBucketDTO.builder()
                .symbol(entity.getSymbol())
                .date(entity.getDate())
                .ticks(tickDTOs)
                .build();
    }
}
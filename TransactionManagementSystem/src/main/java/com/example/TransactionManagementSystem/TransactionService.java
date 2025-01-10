package com.example.TransactionManagementSystem;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    
    public void initializeDatabase() {
        String url = "https://s3.amazonaws.com/roxiler.com/product_transaction.json";
        RestTemplate restTemplate = new RestTemplate();
        Transactionentity[] transactions = restTemplate.getForObject(url, Transactionentity[].class);

        if (transactions != null) {
            for (Transactionentity transaction : transactions) {
                try {
                    
                    if (transaction.getDateOfSale() == null) {
                        String dateAsString = transaction.getDateOfSale() != null ? transaction.getDateOfSale().toString() : null;
                        if (dateAsString != null) {
                           
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
                            transaction.setDateOfSale(OffsetDateTime.parse(dateAsString, formatter));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing date for transaction: " + transaction);
                    e.printStackTrace();
                }
            }
            repository.saveAll(Arrays.asList(transactions));
        }
    }

   
    public List<Transactionentity> getTransactions(String search, int page, int size, int month) {
        if (page < 0) page = 0; 
        if (size <= 0) size = 20;

        Pageable pageable = PageRequest.of(page, size);

        if (search == null || search.isEmpty()) {
            return repository.findAll(pageable).getContent();
        }

        return repository.findByProductTitleContainingOrProductDescriptionContainingOrPrice(
                search, search, search.isEmpty() ? null : 0.0, pageable).getContent();
    }

    
    public StatisticsDTO getStatistics(int month) {
        Double totalSales = repository.getTotalSaleAmount(month);
        Long soldItems = repository.getTotalSoldItems(month);
        Long unsoldItems = repository.getTotalUnsoldItems(month);

        return new StatisticsDTO(totalSales, soldItems, unsoldItems);
    }

    
    public List<BarChartDTO> getBarChart(int month) {
        List<Object[]> rawData = repository.getPriceRangeCounts(month);
        List<BarChartDTO> barChartDTOs = new ArrayList<>();

        
        Map<String, Long> priceRanges = new LinkedHashMap<>();
        priceRanges.put("0-100", 0L);
        priceRanges.put("101-200", 0L);
        priceRanges.put("201-300", 0L);
        priceRanges.put("301-400", 0L);
        priceRanges.put("401-500", 0L);
        priceRanges.put("501-600", 0L);
        priceRanges.put("601-700", 0L);
        priceRanges.put("701-800", 0L);
        priceRanges.put("801-900", 0L);
        priceRanges.put("901-above", 0L);

        
        if (rawData != null && !rawData.isEmpty()) {
            rawData.forEach(rangeData -> {
                Integer rangeBucket = (Integer) rangeData[0];
                Long count = (Long) rangeData[1];

                String rangeKey = switch (rangeBucket) {
                    case 0 -> "0-100";
                    case 1 -> "101-200";
                    case 2 -> "201-300";
                    case 3 -> "301-400";
                    case 4 -> "401-500";
                    case 5 -> "501-600";
                    case 6 -> "601-700";
                    case 7 -> "701-800";
                    case 8 -> "801-900";
                    default -> "901-above";
                };

                priceRanges.put(rangeKey, priceRanges.get(rangeKey) + count);
            });
        }

       
        priceRanges.forEach((key, value) -> barChartDTOs.add(new BarChartDTO(key, value)));

        return barChartDTOs;
    }


    public List<PieChartDTO> getPieChart(int month) {
        List<Object[]> rawData = repository.getCategoryCounts(month);
        List<PieChartDTO> pieChartDTOs = new ArrayList<>();

        if (rawData != null && !rawData.isEmpty()) {
            pieChartDTOs = rawData.stream()
                    .map(obj -> {
                        Object[] categoryData = (Object[]) obj;
                        String category = (String) categoryData[0];
                        Long count = (Long) categoryData[1];
                        return new PieChartDTO(category, count);
                    })
                    .collect(Collectors.toList());
        }

        return pieChartDTOs;
    }
}

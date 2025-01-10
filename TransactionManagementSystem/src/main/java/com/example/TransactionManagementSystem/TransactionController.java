package com.example.TransactionManagementSystem;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    
    @Autowired
    private TransactionService service;

    
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeDatabase() {
        try {
            service.initializeDatabase();
            logger.info("Database initialized successfully.");
            return ResponseEntity.ok("Database initialized successfully!");
        } catch (Exception e) {
            logger.error("Error initializing the database: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initializing the database");
        }
    }

    
    @GetMapping
    public ResponseEntity<List<Transactionentity>> getTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam int month) {
        try {
            // Validation for month range (1-12)
            if (month < 1 || month > 12) {
                logger.warn("Invalid month parameter: {}", month);
                return ResponseEntity.badRequest().body(null);
            }
            List<Transactionentity> transactions = service.getTransactions(search, page, size, month);
            logger.info("Retrieved {} transactions with search query '{}'.", transactions.size(), search);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error retrieving transactions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

   
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(@RequestParam(required = false, defaultValue = "1") Integer month) {
        try {
            
            if (month < 1 || month > 12) {
                logger.warn("Invalid month parameter: {}", month);
                return ResponseEntity.badRequest().body("Month must be between 1 and 12.");
            }

            StatisticsDTO stats = service.getStatistics(month);
            logger.info("Retrieved statistics for month {}", month);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving statistics for month {}: ", month, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving statistics.");
        }
    }

    
    @GetMapping("/bar-chart")
    public ResponseEntity<?> getBarChart(@RequestParam(required = false, defaultValue = "1") Integer month) {
        try {
            
            if (month < 1 || month > 12) {
                logger.warn("Invalid month parameter: {}", month);
                return ResponseEntity.badRequest().body("Month must be between 1 and 12.");
            }

            List<BarChartDTO> barChartData = service.getBarChart(month);
            logger.info("Retrieved bar chart data for month {}", month);
            return ResponseEntity.ok(barChartData);
        } catch (Exception e) {
            logger.error("Error retrieving bar chart data for month {}: ", month, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving bar chart data.");
        }
    }

    
    @GetMapping("/pie-chart")
    public ResponseEntity<?> getPieChart(@RequestParam(required = false, defaultValue = "1") Integer month) {
        try {
           
            if (month < 1 || month > 12) {
                logger.warn("Invalid month parameter: {}", month);
                return ResponseEntity.badRequest().body("Month must be between 1 and 12.");
            }

            List<PieChartDTO> pieChartData = service.getPieChart(month);
            logger.info("Retrieved pie chart data for month {}", month);
            return ResponseEntity.ok(pieChartData);
        } catch (Exception e) {
            logger.error("Error retrieving pie chart data for month {}: ", month, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving pie chart data.");
        }
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
        logger.error("Validation error: " + errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}

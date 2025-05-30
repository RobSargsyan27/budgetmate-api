package budgetMate.api.config;

import budgetMate.api.repository.RecordCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabasePingRunner implements CommandLineRunner {
    private final RecordCategoryRepository repository;

    @Override
    public void run(String... args) {
        log.info("Pinging the database using JPA repository...");

        try {
            long count = repository.count();
            log.info("Database is reachable! Total records in SampleEntity: {}", count);
        } catch (Exception e) {
            log.info("Error while pinging the database: {}", e.getMessage());
            System.exit(1);
        }
    }
}

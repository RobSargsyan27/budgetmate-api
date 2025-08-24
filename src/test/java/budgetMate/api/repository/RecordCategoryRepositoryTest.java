package budgetMate.api.repository;

import budgetMate.api.domain.RecordCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RecordCategoryRepositoryTest {
    private final RecordCategoryRepository recordCategoryRepository;
    private final EntityManager em;

    private RecordCategory food;
    private RecordCategory travel;
    private RecordCategory utilities;

    @BeforeEach
    void setUp() {
        food = RecordCategory.builder().name("Food").description("Groceries, Dining").build();
        travel = RecordCategory.builder().name("Travel").description("Flights, Hotels").build();
        utilities = RecordCategory.builder().name("Utilities").description("Electricity, Water").build();

        recordCategoryRepository.saveAll(List.of(food, travel, utilities));

        em.flush();
        em.clear();
    }

    @Test
    void getAllRecordCategories_returnsAllSeededRows() {
        List<RecordCategory> all = recordCategoryRepository.getAllRecordCategories();
        List<String> categoriesName = all.stream().map(RecordCategory::getName).toList();

        assertThat(categoriesName).containsExactlyInAnyOrder("Food", "Travel", "Utilities");
    }

    @Test
    void getRecordCategoryByName_findsExactMatch_andEmptyWhenMissing() {
        assertThat(recordCategoryRepository.getRecordCategoryByName("Food"))
                .isPresent()
                .get()
                .extracting(RecordCategory::getDescription)
                .isEqualTo("Groceries, Dining");

        assertThat(recordCategoryRepository.getRecordCategoryByName("NonExisting"))
                .isNotPresent();
    }

    @Test
    void getRecordCategoryByNames_returnsSubsetMatchingProvidedNames() {
        List<RecordCategory> subset = recordCategoryRepository.getRecordCategoryByNames(List.of("Travel", "Food"));
        List<String> categoriesName = subset.stream().map(RecordCategory::getName).toList();

        assertThat(categoriesName).containsExactlyInAnyOrder("Food", "Travel");

        List<RecordCategory> none = recordCategoryRepository.getRecordCategoryByNames(List.of("Unknown1", "Unknown2"));
        assertThat(none).isEmpty();
    }
}

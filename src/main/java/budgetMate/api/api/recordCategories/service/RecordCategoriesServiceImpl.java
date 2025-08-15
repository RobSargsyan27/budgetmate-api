package budgetMate.api.api.recordCategories.service;

import budgetMate.api.domain.RecordCategory;
import budgetMate.api.repository.RecordCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordCategoriesServiceImpl implements RecordCategoriesService {
    private final RecordCategoryRepository recordCategoryRepository;

    /**
     * <h2>Get record categories.</h2>
     * @return {List<RecordCategory>}
     */
    @Override
    @Transactional
    public List<RecordCategory> getRecordCategories(){
        return recordCategoryRepository.getAllRecordCategories();
    }

}

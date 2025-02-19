package com.github.RobSargsyan27.budgetMateV2.app.repository.recordCategoryRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;

import java.util.List;

public interface RecordCategoryRepository {

    List<RecordCategory> getAllRecordCategories();

    List<RecordCategory> getRecordCategoryByName(String name);

    long count();
}

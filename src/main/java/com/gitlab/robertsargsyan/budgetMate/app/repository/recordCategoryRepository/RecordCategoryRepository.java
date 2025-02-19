package com.gitlab.robertsargsyan.budgetMate.app.repository.recordCategoryRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.RecordCategory;

import java.util.List;

public interface RecordCategoryRepository {

    List<RecordCategory> getAllRecordCategories();

    List<RecordCategory> getRecordCategoryByName(String name);

    long count();
}

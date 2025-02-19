package com.github.RobSargsyan27.budgetMateV2.app.repository.recordCategoryRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.RecordCategory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Profile("prod")
@Repository
public interface RecordCategoryRepositoryJpaImpl extends JpaRepository<RecordCategory, UUID>, RecordCategoryRepository {

    @Query("SELECT rc FROM RecordCategory rc")
    List<RecordCategory> getAllRecordCategories();

    @Query("SELECT rc FROM RecordCategory rc WHERE upper(rc.name) = upper(:name) ")
    List<RecordCategory> getRecordCategoryByName(String name);
}

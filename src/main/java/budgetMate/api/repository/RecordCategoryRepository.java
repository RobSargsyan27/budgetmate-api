package budgetMate.api.repository;

import budgetMate.api.domain.RecordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecordCategoryRepository extends JpaRepository<RecordCategory, UUID> {
    @Query("SELECT rc FROM RecordCategory rc")
    List<RecordCategory> getAllRecordCategories();

    @Query("SELECT rc FROM RecordCategory rc WHERE upper(rc.name) = upper(:name) ")
    List<RecordCategory> getRecordCategoryByName(String name);
}

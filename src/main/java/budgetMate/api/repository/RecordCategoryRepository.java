package budgetMate.api.repository;

import budgetMate.api.domain.RecordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordCategoryRepository extends JpaRepository<RecordCategory, UUID> {
    @Query("SELECT rc FROM RecordCategory rc")
    List<RecordCategory> getAllRecordCategories();

    @Query("SELECT rc FROM RecordCategory rc WHERE rc.name = :name")
    Optional<RecordCategory> getRecordCategoryByName(String name);

    @Query("SELECT rc FROM RecordCategory rc WHERE rc.name IN :names")
    List<RecordCategory> getRecordCategoryByNames(List<String> names);
}

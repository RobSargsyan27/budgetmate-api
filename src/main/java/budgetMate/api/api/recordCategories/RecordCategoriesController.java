package budgetMate.api.api.recordCategories;

import budgetMate.api.api.recordCategories.service.RecordCategoriesService;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record-categories")
public class RecordCategoriesController {
    private final RecordCategoriesService recordCategoriesService;
    private final HttpUtil httpUtil;

    @GetMapping("")
    public ResponseEntity<List<RecordCategory>> getRecordCategories(){
        return httpUtil.handleGet(recordCategoriesService.getRecordCategories());
    }
}

package budgetMate.api.api.recordCategories;

import budgetMate.api.api.recordCategories.service.RecordCategoriesService;
import budgetMate.api.domain.RecordCategory;
import budgetMate.api.util.HttpUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecordCategoriesController.class)
class RecordCategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecordCategoriesService recordCategoriesService;
    @MockBean
    private HttpUtil httpUtil;

    @Test
    void getRecordCategories_returnsOk() throws Exception {
        List<RecordCategory> categories = List.of(new RecordCategory());
        when(recordCategoriesService.getRecordCategories()).thenReturn(categories);
        when(httpUtil.handleGet(categories)).thenReturn(ResponseEntity.ok(categories));

        mockMvc.perform(get("/api/v1/record-categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getRecordCategories_returnsNoContent() throws Exception {
        when(recordCategoriesService.getRecordCategories()).thenReturn(null);
        when(httpUtil.handleGet(Mockito.<List<RecordCategory>>any())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(get("/api/v1/record-categories"))
                .andExpect(status().isNoContent());
    }
}

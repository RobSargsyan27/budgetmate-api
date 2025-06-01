package budgetMate.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class FileUtil {
    @Async
    public void fileCleanUp(File file) {
        if (file.exists() && file.delete()) {
            log.info("Generated file is deleted!");
        } else {
            log.info("Filed to delete generated file!");
        }
    }
}

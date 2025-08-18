package budgetMate.api.api.records.mapper;


import budgetMate.api.api.accounts.mapper.AccountResponseMapper;
import budgetMate.api.api.records.response.RecordReportResponse;
import budgetMate.api.api.users.mapper.UserResponseMapper;
import budgetMate.api.domain.Record;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserResponseMapper.class, AccountResponseMapper.class})
public interface RecordReportResponseMapper {

    RecordReportResponse toDto(Record record);

    List<RecordReportResponse> toDtoList(List<Record> records);
}

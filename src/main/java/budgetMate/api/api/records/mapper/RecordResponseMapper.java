package budgetMate.api.api.records.mapper;

import budgetMate.api.api.records.response.RecordResponse;
import budgetMate.api.domain.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecordResponseMapper {

    @Mapping(source = "record.user.id", target = "userId")
    @Mapping(source = "record.receivingAccount.name", target = "receivingAccountName")
    @Mapping(source = "record.receivingAccount.id", target = "receivingAccountId")
    @Mapping(source = "record.withdrawalAccount.name", target = "withdrawalAccountName")
    @Mapping(source = "record.withdrawalAccount.id", target = "withdrawalAccountId")
    RecordResponse toDto(Record record);

    List<RecordResponse> toDtoList(List<Record> records);
}

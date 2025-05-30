package budgetMate.api.domain;

import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private double currentBalance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private String avatarColor;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    public void setName(String name) {
        if(name != null){
            this.name = name;
        }
    }

    public void setType(AccountType type) {
        if(type != null){
            this.type = type;
        }
    }

    public void setCurrency(Currency currency) {
        this.currency = currency == null ? Currency.USD : currency;
    }

    public void setAvatarColor(String avatarColor) {
        this.avatarColor = avatarColor == null ? "#581845" : avatarColor;
    }
}

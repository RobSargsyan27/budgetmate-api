package budgetMate.api.domain;

import budgetMate.api.domain.enums.AccountType;
import budgetMate.api.domain.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "current_balance", nullable = false)
    private double currentBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountType type;

    @Column(name = "avatar_color")
    private String avatarColor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_accounts_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdBy;

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setType(AccountType type) {
        if (type != null) {
            this.type = type;
        }
    }

    public void setCurrency(Currency currency) {
        this.currency = (currency == null ? Currency.USD : currency);
    }

    public void setAvatarColor(String avatarColor) {
        this.avatarColor = (avatarColor == null ? "#581845" : avatarColor);
    }
}

package budgetMate.api.domain;

import budgetMate.api.domain.enums.Currency;
import budgetMate.api.domain.enums.RecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_records_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_records_category"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RecordCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RecordType type;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_account_id", foreignKey = @ForeignKey(name = "fk_records_withdrawal_account"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account withdrawalAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reciving_account_id", foreignKey = @ForeignKey(name = "fk_records_reciving_account"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account receivingAccount;

    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        }
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        if (paymentTime != null) {
            this.paymentTime = paymentTime;
        }
    }

    public void setCategory(RecordCategory category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void setNote(String note) {
        if (note != null) {
            this.note = note;
        }
    }
}

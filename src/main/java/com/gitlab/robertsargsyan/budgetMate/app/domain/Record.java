package com.gitlab.robertsargsyan.budgetMate.app.domain;

import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.Currency;
import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.RecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

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

    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private RecordCategory category;

    @Enumerated(EnumType.STRING)
    private RecordType type;

    private String note;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "withdrawal_account_id")
    private Account withdrawalAccount;

    @OneToOne
    @JoinColumn(name = "reciving_account_id")
    private Account receivingAccount;

    public void setAmount(double amount) {
        if(amount > 0){
            this.amount = amount;
        }
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        if(paymentTime != null){
            this.paymentTime = paymentTime;
        }
    }

    public void setCategory(RecordCategory category) {
        if(category != null){
            this.category = category;
        }
    }

    public void setNote(String note) {
        if(note != null){
            this.note = note;
        }
    }
}

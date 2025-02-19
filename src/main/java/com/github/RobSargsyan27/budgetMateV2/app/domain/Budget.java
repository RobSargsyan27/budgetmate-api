package com.github.RobSargsyan27.budgetMateV2.app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "budget_record_categories",
            joinColumns = @JoinColumn(name = "budget_id"),
            inverseJoinColumns = @JoinColumn(name = "record_category_id")
    )
    private List<RecordCategory> recordCategories;

    public void addRecordCategory(RecordCategory recordCategory) {
        if (recordCategories == null) {
            recordCategories = new ArrayList<>();
        }

        recordCategories.add(recordCategory);
    }

    public void setName(String name) {
        if(name != null){
            this.name = name;
        }
    }

    public void setAmount(double amount) {
        this.amount = amount < 1 ? 1 : amount;
    }

    public void setRecordCategories(List<RecordCategory> recordCategories) {
        if(recordCategories != null && !recordCategories.isEmpty()){
            this.recordCategories = recordCategories;
        }
    }
}

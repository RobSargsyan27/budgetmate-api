package budgetMate.api.domain;

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
@Table(name = "account_addition_requests")
public class AccountAdditionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_owner_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User ownerUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "requested_user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_requested_user")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User requestedUser;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "is_request_approved", nullable = false)
    private boolean isRequestApproved = false;

    @Column(name = "is_request_checked", nullable = false)
    private boolean isRequestChecked = false;
}

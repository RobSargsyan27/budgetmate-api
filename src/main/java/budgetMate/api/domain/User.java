package budgetMate.api.domain;

import budgetMate.api.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_accounts",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "account_id",
                    referencedColumnName = "id"
            )
    )
    private List<Account> accounts;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "avatar_color")
    private String avatarColor;

    @Column(name = "receive_news_letters")
    private boolean receiveNewsLetters;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.USER.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }

        accounts.add(account);
    }

    public void setFirstname(String firstname) {
        if(firstname != null){
            this.firstname = firstname;
        }
    }

    public void setLastname(String lastname) {
        if(lastname != null){
            this.lastname = lastname;
        }
    }

    public void setAddress(String address) {
        if(address != null){
            this.address = address;
        }
    }

    public void setCity(String city) {
        if(city != null){
            this.city = city;
        }
    }

    public void setPostalCode(String postalCode) {
        if(postalCode != null){
            this.postalCode = postalCode;
        }
    }

    public void setCountry(String country) {
        if(country != null){
            this.country = country;
        }
    }

    public void setAvatarColor(String avatarColor) {
        if(avatarColor != null){
            this.avatarColor = avatarColor;
        }
    }
}

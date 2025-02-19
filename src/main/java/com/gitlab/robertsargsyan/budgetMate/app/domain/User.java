package com.gitlab.robertsargsyan.budgetMate.app.domain;

import com.gitlab.robertsargsyan.budgetMate.app.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
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

    private String username;

    private String firstname;

    private String lastname;

    private String password;

    private String country;

    private String city;

    private String address;

    private String postalCode;

    private String avatarColor;

    private boolean receiveNewsLetters;

    private boolean isLocked;

    private boolean isEnabled;

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

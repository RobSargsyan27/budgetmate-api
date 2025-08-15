package budgetMate.api.api.users.response;

import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;

    private String username;

    private String firstname;

    private String lastname;

    private String country;

    private String city;

    private String address;

    private String postalCode;

    private String avatarColor;

    private Role role;

    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .country(user.getCountry())
                .city(user.getCity())
                .address(user.getAddress())
                .postalCode(user.getPostalCode())
                .avatarColor(user.getAvatarColor())
                .role(user.getRole())
                .build();
    }

    public static List<UserResponse> from(List<User> users){
        return users.isEmpty() ? List.of() : users.stream().map(UserResponse::from).toList();
    }
}

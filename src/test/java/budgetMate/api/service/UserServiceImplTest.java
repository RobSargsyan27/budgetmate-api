package budgetMate.api.service;

import budgetMate.api.api.accountAdditionRequests.mapper.AccountAdditionResponseMapper;
import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.users.mapper.UserResponseMapper;
import budgetMate.api.api.users.request.UpdateUserRequest;
import budgetMate.api.api.users.response.UserResponse;
import budgetMate.api.api.users.service.UserServiceImpl;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.domain.enums.Role;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountAdditionRequestRepository accountAdditionRequestRepository;
    @Mock
    private UserLib userLib;
    @Mock
    private AccountAdditionResponseMapper accountAdditionResponseMapper;
    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_returnsMappedResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        UserResponse response = new UserResponse(user.getId(), "john", "John", "Doe", null, null, null, null, null, Role.USER);

        when(userLib.fetchRequestUser(request)).thenReturn(user);
        when(userResponseMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.getUser(request);

        assertThat(result).isEqualTo(response);
        verify(userLib).fetchRequestUser(request);
        verify(userResponseMapper).toDto(user);
    }

    @Test
    void updateUser_updatesFieldsAndReturnsResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        UpdateUserRequest body = mock(UpdateUserRequest.class);
        when(body.getFirstname()).thenReturn("NewFirst");
        when(body.getLastname()).thenReturn("NewLast");
        when(body.getCountry()).thenReturn("NewCountry");
        when(body.getCity()).thenReturn("NewCity");
        when(body.getAddress()).thenReturn("NewAddress");
        when(body.getPostalCode()).thenReturn("NewPostal");
        when(body.getAvatarColor()).thenReturn("#FFFFFF");

        User savedUser = User.builder()
                .id(user.getId())
                .firstname("NewFirst")
                .lastname("NewLast")
                .country("NewCountry")
                .city("NewCity")
                .address("NewAddress")
                .postalCode("NewPostal")
                .avatarColor("#FFFFFF")
                .build();
        UserResponse response = new UserResponse(savedUser.getId(), null, "NewFirst", "NewLast", "NewCountry", "NewCity", "NewAddress", "NewPostal", "#FFFFFF", null);

        when(userLib.fetchRequestUser(request)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userResponseMapper.toDto(savedUser)).thenReturn(response);

        UserResponse result = userService.updateUser(request, body);

        assertThat(result).isEqualTo(response);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User updated = captor.getValue();
        assertThat(updated.getFirstname()).isEqualTo("NewFirst");
        assertThat(updated.getLastname()).isEqualTo("NewLast");
        assertThat(updated.getCountry()).isEqualTo("NewCountry");
        assertThat(updated.getCity()).isEqualTo("NewCity");
        assertThat(updated.getAddress()).isEqualTo("NewAddress");
        assertThat(updated.getPostalCode()).isEqualTo("NewPostal");
        assertThat(updated.getAvatarColor()).isEqualTo("#FFFFFF");
    }

    @Test
    void deleteUser_deletesById() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        when(userLib.fetchRequestUser(request)).thenReturn(user);

        Void result = userService.deleteUser(request);

        assertThat(result).isNull();
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void getUserNotifications_returnsResponses() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        AccountAdditionRequest req = AccountAdditionRequest.builder().id(UUID.randomUUID()).build();
        List<AccountAdditionRequest> requests = List.of(req);
        List<AccountAdditionResponse> responses = List.of(AccountAdditionResponse.builder().id(req.getId()).build());

        when(userLib.fetchRequestUser(request)).thenReturn(user);
        when(accountAdditionRequestRepository.findUnapprovedRequestsByOwnerUser(user)).thenReturn(requests);
        when(accountAdditionResponseMapper.toDtoList(requests)).thenReturn(responses);

        List<AccountAdditionResponse> result = userService.getUserNotifications(request);

        assertThat(result).isEqualTo(responses);
        verify(accountAdditionRequestRepository).findUnapprovedRequestsByOwnerUser(user);
        verify(accountAdditionResponseMapper).toDtoList(requests);
    }
}
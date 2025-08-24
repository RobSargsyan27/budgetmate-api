package budgetMate.api.api;

import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.users.UserController;
import budgetMate.api.api.users.request.UpdateUserRequest;
import budgetMate.api.api.users.response.UserResponse;
import budgetMate.api.api.users.service.UserService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpUtil httpUtil;

    @InjectMocks
    private UserController userController;

    @Test
    void getUser_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "john", "John", "Doe", null, null, null, null, null, null);
        ResponseEntity<UserResponse> expected = ResponseEntity.ok(userResponse);

        when(userService.getUser(request)).thenReturn(userResponse);
        when(httpUtil.handleGet(userResponse)).thenReturn(expected);

        ResponseEntity<UserResponse> result = userController.getUser(request);

        assertThat(result).isEqualTo(expected);
        verify(userService).getUser(request);
        verify(httpUtil).handleGet(userResponse);
    }

    @Test
    void updateUser_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UpdateUserRequest body = mock(UpdateUserRequest.class);
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "john", "John", "Doe", null, null, null, null, null, null);
        ResponseEntity<UserResponse> expected = ResponseEntity.ok(userResponse);

        when(userService.updateUser(request, body)).thenReturn(userResponse);
        when(httpUtil.handleUpdate(userResponse)).thenReturn(expected);

        ResponseEntity<UserResponse> result = userController.updateUser(request, body);

        assertThat(result).isEqualTo(expected);
        verify(userService).updateUser(request, body);
        verify(httpUtil).handleUpdate(userResponse);
    }

    @Test
    void getUserNotifications_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        List<AccountAdditionResponse> responses = List.of(AccountAdditionResponse.builder().id(UUID.randomUUID()).build());
        ResponseEntity<List<AccountAdditionResponse>> expected = ResponseEntity.ok(responses);

        when(userService.getUserNotifications(request)).thenReturn(responses);
        when(httpUtil.handleGet(responses)).thenReturn(expected);

        ResponseEntity<List<AccountAdditionResponse>> result = userController.getUserNotifications(request);

        assertThat(result).isEqualTo(expected);
        verify(userService).getUserNotifications(request);
        verify(httpUtil).handleGet(responses);
    }
}
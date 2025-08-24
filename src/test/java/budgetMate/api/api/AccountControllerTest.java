package budgetMate.api.api;

import budgetMate.api.api.accounts.AccountController;
import budgetMate.api.api.accounts.request.AddAccountRequest;
import budgetMate.api.api.accounts.request.UpdateAccountRequest;
import budgetMate.api.api.accounts.response.AccountResponse;
import budgetMate.api.api.accounts.service.AccountService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private HttpUtil httpUtil;

    @InjectMocks
    private AccountController accountController;

    @Test
    void getUserAccounts_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        List<AccountResponse> responses = List.of(AccountResponse.builder().id(UUID.randomUUID()).build());
        ResponseEntity<List<AccountResponse>> expected = ResponseEntity.ok(responses);

        when(accountService.getUserAccounts(request)).thenReturn(responses);
        when(httpUtil.handleGet(responses)).thenReturn(expected);

        ResponseEntity<List<AccountResponse>> result = accountController.getUserAccounts(request);

        assertThat(result).isEqualTo(expected);
        verify(accountService).getUserAccounts(request);
        verify(httpUtil).handleGet(responses);
    }

    @Test
    void getUserAccounts_whenServiceThrows_exceptionPropagated() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RuntimeException ex = new RuntimeException("error");

        when(accountService.getUserAccounts(request)).thenThrow(ex);

        assertThatThrownBy(() -> accountController.getUserAccounts(request))
                .isSameAs(ex);

        verify(accountService).getUserAccounts(request);
        verifyNoInteractions(httpUtil);
    }

    @Test
    void addUserAccount_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AddAccountRequest body = mock(AddAccountRequest.class);
        AccountResponse response = AccountResponse.builder().id(UUID.randomUUID()).build();
        ResponseEntity<AccountResponse> expected = ResponseEntity.ok(response);

        when(accountService.addUserAccount(request, body)).thenReturn(response);
        when(httpUtil.handleAdd(response)).thenReturn(expected);

        ResponseEntity<AccountResponse> result = accountController.addUserAccount(request, body);

        assertThat(result).isEqualTo(expected);
        verify(accountService).addUserAccount(request, body);
        verify(httpUtil).handleAdd(response);
    }

    @Test
    void addUserAccount_whenServiceThrows_exceptionPropagated() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AddAccountRequest body = mock(AddAccountRequest.class);
        RuntimeException ex = new RuntimeException("error");

        when(accountService.addUserAccount(request, body)).thenThrow(ex);

        assertThatThrownBy(() -> accountController.addUserAccount(request, body))
                .isSameAs(ex);

        verify(accountService).addUserAccount(request, body);
        verifyNoInteractions(httpUtil);
    }

    @Test
    void getUserAccount_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID id = UUID.randomUUID();
        AccountResponse response = AccountResponse.builder().id(id).build();
        ResponseEntity<AccountResponse> expected = ResponseEntity.ok(response);

        when(accountService.getUserAccount(request, id)).thenReturn(response);
        when(httpUtil.handleGet(response)).thenReturn(expected);

        ResponseEntity<AccountResponse> result = accountController.getUserAccount(id, request);

        assertThat(result).isEqualTo(expected);
        verify(accountService).getUserAccount(request, id);
        verify(httpUtil).handleGet(response);
    }

    @Test
    void getUserAccount_whenServiceThrows_exceptionPropagated() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID id = UUID.randomUUID();
        RuntimeException ex = new RuntimeException("error");

        when(accountService.getUserAccount(request, id)).thenThrow(ex);

        assertThatThrownBy(() -> accountController.getUserAccount(id, request))
                .isSameAs(ex);

        verify(accountService).getUserAccount(request, id);
        verifyNoInteractions(httpUtil);
    }

    @Test
    void updateUserAccount_returnsResponseFromHttpUtil() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID id = UUID.randomUUID();
        UpdateAccountRequest body = mock(UpdateAccountRequest.class);
        AccountResponse response = AccountResponse.builder().id(id).build();
        ResponseEntity<AccountResponse> expected = ResponseEntity.ok(response);

        when(accountService.updateUserAccount(request, id, body)).thenReturn(response);
        when(httpUtil.handleUpdate(response)).thenReturn(expected);

        ResponseEntity<AccountResponse> result = accountController.updateUserAccount(request, body, id);

        assertThat(result).isEqualTo(expected);
        verify(accountService).updateUserAccount(request, id, body);
        verify(httpUtil).handleUpdate(response);
    }

    @Test
    void updateUserAccount_whenServiceThrows_exceptionPropagated() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID id = UUID.randomUUID();
        UpdateAccountRequest body = mock(UpdateAccountRequest.class);
        RuntimeException ex = new RuntimeException("error");

        when(accountService.updateUserAccount(request, id, body)).thenThrow(ex);

        assertThatThrownBy(() -> accountController.updateUserAccount(request, body, id))
                .isSameAs(ex);

        verify(accountService).updateUserAccount(request, id, body);
        verifyNoInteractions(httpUtil);
    }

    @Test
    void deleteUserAccount_whenServiceThrows_exceptionPropagated() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UUID id = UUID.randomUUID();
        RuntimeException ex = new RuntimeException("error");

        when(accountService.deleteUserAccount(request, id)).thenThrow(ex);

        assertThatThrownBy(() -> accountController.deleteUserAccount(request, id))
                .isSameAs(ex);

        verify(accountService).deleteUserAccount(request, id);
        verifyNoInteractions(httpUtil);
    }
}

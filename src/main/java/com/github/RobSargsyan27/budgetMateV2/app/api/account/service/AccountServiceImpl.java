package com.github.RobSargsyan27.budgetMateV2.app.api.account.service;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.AddAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.AddExistingAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.account.request.UpdateAccountRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.account.response.AccountResponse;
import com.github.RobSargsyan27.budgetMateV2.app.domain.Account;
import com.github.RobSargsyan27.budgetMateV2.app.domain.AccountAdditionRequest;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import com.github.RobSargsyan27.budgetMateV2.app.lib.UserLib;
import com.github.RobSargsyan27.budgetMateV2.app.repository.AccountAdditionRequestRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.AccountRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.RecordRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;
    private final RecordRepository recordRepository;
    private final UserLib userLib;

    public List<AccountResponse> getAccounts(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        return AccountResponse.from(user.getAccounts());
    }

    public AccountResponse getAccount(String id){
        final Account account =  accountRepository.getAccountById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalStateException("Account not found!"));

        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse addAccount(AddAccountRequest request){
        final User user = userRepository.findUserById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        final Account account = new Account();
        account.setName(request.getName());
        account.setCurrency(request.getCurrency());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setType(request.getType());
        account.setAvatarColor(request.getAvatarColor());
        account.setCreatedBy(user);

        user.addAccount(account);
        final User updatedUser = userRepository.save(user);

        return AccountResponse.from(updatedUser.getAccounts().getLast());
    }

    @Transactional
    public AccountResponse updateAccount(UpdateAccountRequest request, String id){
        final UUID accountId = UUID.fromString(id);

        final Account account = accountRepository.getAccountById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account is not found!"));
        account.setName(request.getName());
        account.setType(request.getType());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setAvatarColor(request.getAvatarColor());

        final Account updatedAccount = accountRepository.save(account);

        return AccountResponse.from(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(HttpServletRequest request, String id){
        final User user = userLib.fetchRequestUser(request);
        final UUID accountId = UUID.fromString(id);

        accountRepository.deleteUserAccountAssociation(user.getId(), accountId);
        recordRepository.deleteAccountRecords(accountId);

        int associationCount = accountRepository.countUsersByAccountId(accountId);
        if(associationCount == 0){
            accountRepository.deleteAccountById(accountId);
        }
    }

    public void sendAddExistingAccountRequest(AddExistingAccountRequest request){
        final UUID userId = UUID.fromString(request.getUserId());
        final String accountOwnerUsername = request.getOwnerUsername();
        final String accountName = request.getAccountName();

        final User accountOwner = userRepository.findUserByUsername(accountOwnerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        final User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        final AccountAdditionRequest accountAdditionRequest = AccountAdditionRequest.builder()
                .ownerUser(accountOwner)
                .requestedUser(user)
                .accountName(accountName)
                .build();

        accountAdditionRequestRepository.save(accountAdditionRequest);
    }

    @Transactional
    public void updateAccountRequestStatus(String requestId, String status){
        final UUID _requestId = UUID.fromString(requestId);
        final boolean _status = Boolean.parseBoolean(status);

        try{
            accountAdditionRequestRepository.updateAccountAdditionRequest(_requestId, _status);

            if(_status){
                final AccountAdditionRequest accountAdditionRequest = accountAdditionRequestRepository
                        .getAccountAdditionRequestById(_requestId)
                        .orElseThrow(() -> new IllegalArgumentException("Account addition request is not found!"));

                final Account account = accountRepository
                        .getUserAccount(accountAdditionRequest.getOwnerUser().getId(), accountAdditionRequest.getAccountName())
                        .orElseThrow(() -> new IllegalArgumentException("Account is not found!"));

                final User user = userRepository
                        .findUserById(accountAdditionRequest.getRequestedUser().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User is not found!"));

                user.addAccount(account);
                userRepository.save(user);
            }
        } finally {
            accountAdditionRequestRepository.updateAccountAdditionRequest(_requestId, _status);
        }
    }
}

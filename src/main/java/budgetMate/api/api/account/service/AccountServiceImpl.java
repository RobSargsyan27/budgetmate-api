package budgetMate.api.api.account.service;

import java.util.List;
import java.util.UUID;

import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.AddExistingAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.RecordRepository;
import budgetMate.api.repository.UserRepository;

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

    public AccountResponse getAccount(UUID id, HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);
        final Account account =  accountRepository.getUserAccountById(user, id)
                .orElseThrow(() -> new IllegalStateException("Account not found!"));

        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse addAccount(HttpServletRequest request, AddAccountRequest body){
        final User user = userLib.fetchRequestUser(request);

        final Account account = new Account();
        account.setName(body.getName());
        account.setCurrency(body.getCurrency());
        account.setCurrentBalance(body.getCurrentBalance());
        account.setType(body.getType());
        account.setAvatarColor(body.getAvatarColor());
        account.setCreatedBy(user);

        user.addAccount(account);
        final User updatedUser = userRepository.save(user);

        return AccountResponse.from(updatedUser.getAccounts().getLast());
    }

    @Transactional
    public AccountResponse updateAccount(HttpServletRequest request, UpdateAccountRequest body, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Account account = accountRepository.getUserAccountById(user, id)
                .orElseThrow(() -> new IllegalStateException("Account is not found!"));

        account.setName(body.getName());
        account.setType(body.getType());
        account.setCurrentBalance(body.getCurrentBalance());
        account.setAvatarColor(body.getAvatarColor());

        final Account updatedAccount = accountRepository.save(account);

        return AccountResponse.from(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        accountRepository.deleteUserAccountAssociation(user.getId(), id);
        recordRepository.deleteAccountRecords(id);

        int associationCount = accountRepository.countUsersByAccountId(id);
        if(associationCount == 0){
            accountRepository.deleteAccountById(id);
        }
    }

    public void sendAddExistingAccountRequest(HttpServletRequest request, AddExistingAccountRequest body){
        final User user = userLib.fetchRequestUser(request);
        final String accountOwnerUsername = body.getOwnerUsername();
        final String accountName = body.getAccountName();

        final User accountOwner = userRepository.findUserByUsername(accountOwnerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        final AccountAdditionRequest accountAdditionRequest = AccountAdditionRequest.builder()
                .ownerUser(accountOwner)
                .requestedUser(user)
                .accountName(accountName)
                .build();

        accountAdditionRequestRepository.save(accountAdditionRequest);
    }

    @Transactional
    public void updateAccountRequestStatus(HttpServletRequest request, UUID requestId, Boolean status){
        final User user = userLib.fetchRequestUser(request);

        try{
            accountAdditionRequestRepository.updateAccountAdditionRequest(user, requestId, status);

            if(status){
                final AccountAdditionRequest accountAdditionRequest = accountAdditionRequestRepository
                        .getAccountAdditionRequestById(requestId)
                        .orElseThrow(() -> new IllegalArgumentException("Account addition request is not found!"));

                final Account account = accountRepository
                        .getUserAccount(accountAdditionRequest.getOwnerUser().getId(), accountAdditionRequest.getAccountName())
                        .orElseThrow(() -> new IllegalArgumentException("Account is not found!"));

                final User requestedUser = userRepository
                        .findUserById(accountAdditionRequest.getRequestedUser().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User is not found!"));

                requestedUser.addAccount(account);
                userRepository.save(requestedUser);
            }
        } finally {
            accountAdditionRequestRepository.updateAccountAdditionRequest(user, requestId, status);
        }
    }
}

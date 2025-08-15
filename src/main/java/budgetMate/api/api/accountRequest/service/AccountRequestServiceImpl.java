package budgetMate.api.api.accountRequest.service;

import budgetMate.api.api.accountRequest.request.*;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {
    private final UserLib userLib;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;

    public Void addExistingAccountRequest(HttpServletRequest request, AddExistingAccountRequest body){
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
        return null;
    }

    public Void updateExistingAccountRequest(
            HttpServletRequest request, UUID requestId, UpdateExistingAccountRequest body
    ){
        final User user = userLib.fetchRequestUser(request);
        final Boolean status = body.getStatus();

        try{
            accountAdditionRequestRepository.updateAccountAdditionRequest(requestId, user, status);

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
            accountAdditionRequestRepository.updateAccountAdditionRequest(requestId, user, status);
        }
        return null;
    }
}

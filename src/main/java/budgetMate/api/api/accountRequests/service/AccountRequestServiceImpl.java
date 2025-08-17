package budgetMate.api.api.accountRequests.service;

import budgetMate.api.api.accountRequests.request.*;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.lib.FetchLib;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {
    private final UserLib userLib;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;
    private final FetchLib fetchLib;

    public Void addExistingAccountRequest(HttpServletRequest request, AddExistingAccountRequest body){
        final User user = userLib.fetchRequestUser(request);
        final String accountOwnerUsername = body.getOwnerUsername();
        final String accountName = body.getAccountName();

        final User accountOwner = fetchLib.fetchResource(userRepository.findUserByUsername(accountOwnerUsername), "User");

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
                final AccountAdditionRequest accountAdditionRequest = fetchLib.fetchResource(
                                accountAdditionRequestRepository.getAccountAdditionRequestById(requestId),
                                "Account Addition Request");

                final Account account = fetchLib.fetchResource(accountRepository.getUserAccount(
                        accountAdditionRequest.getOwnerUser().getId(), accountAdditionRequest.getAccountName()),
                        "Account");

                final User requestedUser = fetchLib.fetchResource(
                        userRepository.findUserById(accountAdditionRequest.getRequestedUser().getId()),
                        "User");

                requestedUser.addAccount(account);
                userRepository.save(requestedUser);
            }
        } finally {
            accountAdditionRequestRepository.updateAccountAdditionRequest(requestId, user, status);
        }
        return null;
    }
}

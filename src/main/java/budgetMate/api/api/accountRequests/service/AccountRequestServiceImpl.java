package budgetMate.api.api.accountRequests.service;

import budgetMate.api.api.accountRequests.request.*;
import budgetMate.api.api.accountRequests.response.AccountRequestResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.lib.FetchLib;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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

    /**
     * <h2>Add user account request.</h2>
     *
     * @param request {HttpServletRequest}
     * @param body    {AddUserAccountRequest}
     * @return {AccountRequestResponse}
     */
    @Override
    @Transactional
    public AccountRequestResponse addUserAccountRequest(HttpServletRequest request, AddUserAccountRequest body) {
        final User user = userLib.fetchRequestUser(request);
        final String accountOwnerUsername = body.getOwnerUsername();
        final String accountName = body.getAccountName();

        final User accountOwner = fetchLib.fetchResource(userRepository.findUserByUsername(accountOwnerUsername), "User");

        final AccountAdditionRequest accountAdditionRequest = AccountAdditionRequest.builder()
                .ownerUser(accountOwner)
                .requestedUser(user)
                .accountName(accountName)
                .build();

        return AccountRequestResponse.from(accountAdditionRequestRepository.save(accountAdditionRequest));
    }

    /**
     * <h2>Update user account request.</h2>
     *
     * @param request   {HttpServletRequest}
     * @param requestId {UUID}
     * @param body      {UpdateUserAccountRequest}
     * @return {AccountRequestResponse}
     */
    @Override
    @Transactional
    public AccountRequestResponse updateUserAccountRequest(HttpServletRequest request, UUID requestId, UpdateUserAccountRequest body) {
        final User user = userLib.fetchRequestUser(request);
        final Boolean status = body.getStatus();

        accountAdditionRequestRepository.updateAccountAdditionRequest(user, requestId, status);
        final AccountAdditionRequest accountAdditionRequest = fetchLib.fetchResource(
                accountAdditionRequestRepository.getAccountAdditionRequestById(requestId),
                "Account Addition Request");

        if (status) {
            final Account account = fetchLib.fetchResource(accountRepository.getUserAccount(
                            accountAdditionRequest.getOwnerUser().getId(), accountAdditionRequest.getAccountName()),
                    "Account");

            accountRepository.addUserAccountAssociation(accountAdditionRequest.getRequestedUser().getId(), account.getId());
        }

        return AccountRequestResponse.from(accountAdditionRequest);
    }
}

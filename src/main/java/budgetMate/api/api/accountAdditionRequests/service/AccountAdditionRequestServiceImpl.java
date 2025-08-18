package budgetMate.api.api.accountAdditionRequests.service;

import budgetMate.api.api.accountAdditionRequests.mapper.AccountAdditionResponseMapper;
import budgetMate.api.api.accountAdditionRequests.request.*;
import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.util.FetchUtil;
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
public class AccountAdditionRequestServiceImpl implements AccountAdditionRequestService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;
    private final UserLib userLib;
    private final FetchUtil fetchUtil;
    private final AccountAdditionResponseMapper accountAdditionResponseMapper;

    /**
     * <h2>Add user account request.</h2>
     *
     * @param request {HttpServletRequest}
     * @param body    {AddUserAccountRequest}
     * @return {AccountRequestResponse}
     */
    @Override
    @Transactional
    public AccountAdditionResponse addUserAccountRequest(HttpServletRequest request, AddAccountAdditionRequest body) {
        final User user = userLib.fetchRequestUser(request);
        final String accountOwnerUsername = body.getOwnerUsername();
        final String accountName = body.getAccountName();

        final User accountOwner = fetchUtil.fetchResource(userRepository.findUserByUsername(accountOwnerUsername), "User");

        final AccountAdditionRequest accountAdditionRequest = AccountAdditionRequest.builder()
                .ownerUser(accountOwner)
                .requestedUser(user)
                .accountName(accountName)
                .build();

        return accountAdditionResponseMapper.toDto(accountAdditionRequestRepository.save(accountAdditionRequest));
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
    public AccountAdditionResponse updateUserAccountRequest(HttpServletRequest request, UUID requestId, UpdateAccountAdditionRequest body) {
        final User user = userLib.fetchRequestUser(request);
        final Boolean status = body.getStatus();

        accountAdditionRequestRepository.updateAccountAdditionRequest(user, requestId, status);
        final AccountAdditionRequest accountAdditionRequest = fetchUtil.fetchResource(
                accountAdditionRequestRepository.getAccountAdditionRequestById(requestId),
                "Account Addition Request");

        if (status) {
            final Account account = fetchUtil.fetchResource(accountRepository.getUserAccount(
                            accountAdditionRequest.getOwnerUser().getId(), accountAdditionRequest.getAccountName()),
                    "Account");

            accountRepository.addUserAccountAssociation(accountAdditionRequest.getRequestedUser().getId(), account.getId());
        }

        return accountAdditionResponseMapper.toDto(accountAdditionRequest);
    }
}

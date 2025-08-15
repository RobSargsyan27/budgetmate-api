package budgetMate.api.api.account.service;

import java.util.List;
import java.util.UUID;

import budgetMate.api.api.account.request.AddAccountRequest;
import budgetMate.api.api.account.request.UpdateAccountRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import budgetMate.api.api.account.response.AccountResponse;
import budgetMate.api.domain.Account;
import budgetMate.api.domain.User;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountRepository;
import budgetMate.api.repository.RecordRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final RecordRepository recordRepository;
    private final UserLib userLib;

    /**
     * <h2>Get user accounts.</h2>
     * @param request {HttpServletRequest}
     * @return {List<AccountResponse>}
     */
    @Override
    @Transactional
    public List<AccountResponse> getUserAccounts(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<Account> accounts = accountRepository.getUserAccounts(user);

        return AccountResponse.from(accounts);
    }

    /**
     * <h2>Add user account.</h2>
     * @param request {HttpServletRequest}
     * @param body {AddAccountRequest}
     * @return {AccountResponse}
     */
    @Override
    @Transactional
    public AccountResponse addUserAccount(HttpServletRequest request, AddAccountRequest body){
        final User user = userLib.fetchRequestUser(request);

        final Account account = new Account();
        account.setName(body.getName());
        account.setCurrency(body.getCurrency());
        account.setCurrentBalance(body.getCurrentBalance());
        account.setType(body.getType());
        account.setAvatarColor(body.getAvatarColor());
        account.setCreatedBy(user);

        final Account addedAccount = accountRepository.save(account);
        accountRepository.addUserAccountAssociation(user.getId(), addedAccount.getId());

        return AccountResponse.from(addedAccount);
    }

    /**
     * <h2>Get user account.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {AccountResponse}
     */
    @Override
    @Transactional
    public AccountResponse getUserAccount(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        final Account account =  accountRepository.getUserAccountById(user, id)
                .orElseThrow(() -> new IllegalStateException("Account not found!"));

        return AccountResponse.from(account);
    }

    /**
     * <h2>Update user account.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @param body {UpdateAccountRequest}
     * @return {AccountResponse}
     */
    @Override
    @Transactional
    public AccountResponse updateUserAccount(HttpServletRequest request, UUID id, UpdateAccountRequest body){
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

    /**
     * <h2>Delete user account.</h2>
     * @param request {HttpServletRequest}
     * @param id {UUID}
     * @return {Void}
     */
    @Override
    @Transactional
    public Void deleteUserAccount(HttpServletRequest request, UUID id){
        final User user = userLib.fetchRequestUser(request);

        accountRepository.deleteUserAccountAssociation(user.getId(), id);
        recordRepository.deleteAccountRecords(id);

        final int associationCount = accountRepository.countUserAccountAssociations(id);
        if(associationCount == 0){
            accountRepository.deleteAccountById(id);
        }

        return null;
    }
}

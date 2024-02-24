package com.byaffe.microtasks.services.impl;

import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.daos.WithdrawRequestDao;
import com.byaffe.microtasks.dtos.WithdrawRequestDTO;
import com.byaffe.microtasks.models.WithdrawRequest;
import com.byaffe.microtasks.models.WithdrawRequestStatus;
import com.byaffe.microtasks.services.CreditService;
import com.byaffe.microtasks.services.LookupValueService;
import com.byaffe.microtasks.services.WithDrawRequestService;
import com.byaffe.microtasks.shared.constants.RecordStatus;
import com.byaffe.microtasks.shared.exceptions.ResourceNotFoundException;
import com.byaffe.microtasks.shared.exceptions.ValidationFailedException;
import com.byaffe.microtasks.shared.utils.CustomSearchUtils;
import com.byaffe.microtasks.shared.utils.PassEncTech4;
import com.byaffe.microtasks.shared.utils.Validate;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class WithdrawRequestServiceImpl implements WithDrawRequestService {
    @Autowired
    WithdrawRequestDao withdrawRequestDao;

    @Autowired
    CreditService creditService;

    @Override
    public WithdrawRequest saveInstance(WithdrawRequestDTO dto) {
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        if (!(dto.getAmount() > 0)) {
            throw new ValidationFailedException("Invalid amount");
        }

        if (!(dto.getAmount() > UserDetailsContext.getLoggedInUser().getBalance())) {
            throw new ValidationFailedException("Invalid amount");
        }

        if (!PassEncTech4.verifyUserPassword(dto.getPassword(), UserDetailsContext.getLoggedInUser().getPassword())) {
            throw new ValidationFailedException("Invalid  Password");
        }
        double tax=5.6;
        withdrawRequest.setAmountRequested(dto.getAmount());
        withdrawRequest.setSystemTax(tax*dto.getAmount());
        withdrawRequest.setNetAmountPayable(withdrawRequest.getAmountRequested()- withdrawRequest.getSystemTax());
        withdrawRequest.setNames(dto.getNames());
        withdrawRequest.setPhoneNumber(UserDetailsContext.getLoggedInUser().getPhoneNumber());
        withdrawRequest.setStatus(WithdrawRequestStatus.SUBMITTED);

        return withdrawRequestDao.save(withdrawRequest);
    }


    @Override
    public void deleteInstance(long id) throws ValidationFailedException {
        WithdrawRequest existsWithId = getInstanceById(id);
        existsWithId.setRecordStatus(RecordStatus.DELETED);
        withdrawRequestDao.save(existsWithId);

    }

    @Override
    public List<WithdrawRequest> getInstances(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return withdrawRequestDao.search(search);
    }

    public long countInstances(Search search) {
        return withdrawRequestDao.count(search);
    }

    public WithdrawRequest disburseWithdrawRequest(Long id) {
        WithdrawRequest withdrawRequest = getInstanceById(id);
        if (!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()) {
            throw new ValidationFailedException("You don't have permissions to do this");
        }

        if (!withdrawRequest.getStatus().equals(WithdrawRequestStatus.SUBMITTED)) {
            throw new ValidationFailedException("You can only activate submitted withdrawRequests");
        }
        withdrawRequest.setStatus(WithdrawRequestStatus.DISBURSED);
        withdrawRequest.setDateCompleted(LocalDateTime.now());
        withdrawRequest=   withdrawRequestDao.save(withdrawRequest);
        creditService.updateUserCredit(withdrawRequest.getCreatedById());
        return withdrawRequest;
    }

    public WithdrawRequest rejectWithdrawRequest(Long id) {
        WithdrawRequest withdrawRequest = getInstanceById(id);
        if (!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()) {
            throw new ValidationFailedException("You don't have permissions to do this");
        }
        if (!withdrawRequest.getStatus().equals(WithdrawRequestStatus.SUBMITTED)) {
            throw new ValidationFailedException("You can only reject submitted withdrawRequests");
        }
        withdrawRequest.setStatus(WithdrawRequestStatus.REJECTED);
        return withdrawRequestDao.save(withdrawRequest);
    }



    public WithdrawRequest cancelWithdrawRequest(Long id) {
        WithdrawRequest withdrawRequest = getInstanceById(id);
        if (!withdrawRequest.getStatus().equals(WithdrawRequestStatus.SUBMITTED)) {
            throw new ValidationFailedException("You can only cancel submitted withdrawRequests");
        }
        withdrawRequest.setStatus(WithdrawRequestStatus.CANCELLED);
        return withdrawRequestDao.save(withdrawRequest);
    }

    @Override
    public WithdrawRequest getInstanceById(long id) {
        return withdrawRequestDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("WithdrawRequest", "id", id));
    }


    public static Search composeSearchObject(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("name", "category.value", "details"));

        return search;
    }

}

package com.yrgo.services.calls;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementMockImpl;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import com.yrgo.services.diary.DiaryManagementServiceMockImpl;

import java.util.Collection;

public class CallHandlingServiceImpl implements CallHandlingService {

    CustomerManagementService customerManagementService;
    DiaryManagementService diaryManagementService;

    public CallHandlingServiceImpl(CustomerManagementService customerManagementService, DiaryManagementService diaryManagementService) {
        this.customerManagementService = customerManagementService;
        this.diaryManagementService = diaryManagementService;
    }

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        customerManagementService.recordCall(customerId, newCall);

        for (Action action : actions) {
            diaryManagementService.recordAction(action);
        }
    }


}

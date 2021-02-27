package com.booster.vocabulary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionWrapperService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeInTransaction(Runnable runnable) {
        runnable.run();
    }

}

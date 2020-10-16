package com.picpay.javaconciliationsdk;

import java.util.Collection;

public interface Outbox {

    public void send(Operation operation);

    void sendUnsentOperations();
}

package com.picpay.javaconciliationsdk;

public interface Outbox {

    public void send(Operation operation);

    void sendUnsentOperations();
}

package com.picpay.javaconciliationsdk;

import java.util.*;
import java.util.stream.*;

public class OutboxMemory implements Outbox {

    private SortedSet<Operation> operations;
    private Conciliation conciliation;
    private int maxOperationsToPoll;

    public OutboxMemory(SortedSet<Operation> operations,
                        Conciliation conciliation,
                        int maxOperationsToPoll){
        this.operations = operations;
        this.conciliation = conciliation;
        this.maxOperationsToPoll = maxOperationsToPoll;
    }

    @Override
    public void send(Operation operation) {
        sendToConciliation(operation);

        operations.add(operation);
    }

    @Override
    public void sendUnsentOperations() {
        operations.stream()
                .filter(o -> o.isUnsent())
                .collect(Collectors.toList())
                .stream()
                .limit(maxOperationsToPoll)
                .collect(Collectors.toList())
                .forEach(this::sendToConciliation);
    }

    private void sendToConciliation(Operation operation){
        operation.markAsSending();
        if(conciliation.send(operation)) operation.markAsSent();
        else operation.markErrorSending();
    }
}

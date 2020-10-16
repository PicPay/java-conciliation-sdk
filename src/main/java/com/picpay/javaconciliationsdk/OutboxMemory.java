package com.picpay.javaconciliationsdk;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.*;

public class OutboxMemory implements Outbox {

    private Map<UUID, Operation> operations;
    private Conciliation conciliation;
    private int maxOperationsToPoll;

    public OutboxMemory(Map<UUID, Operation> operations,
                        Conciliation conciliation,
                        int maxOperationsToPoll){
        this.operations = operations;
        this.conciliation = conciliation;
        this.maxOperationsToPoll = maxOperationsToPoll;
    }

    @Override
    public void send(Operation operation) {
        operation.markAsSending();
        sendToConciliation(operation);

        operations.put(operation.getCID(), operation);
    }

    @Override
    public void sendUnsentOperations() {
        operations.values().stream()
                .filter(o -> o.isUnsent())
                .collect(Collectors.toList())
                .stream()
                .limit(maxOperationsToPoll)
                .collect(Collectors.toList())
                .forEach(this::sendToConciliation);
    }

    private void sendToConciliation(Operation operation){
        if(conciliation.send(operation)) operation.markAsSent();
        else operation.markErrorSending();
    }
}

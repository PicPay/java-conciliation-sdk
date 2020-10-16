package com.picpay.javaconciliationsdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OutboxMemoryTest {

    private Outbox outbox;
    private Operation operation;
    private Map<UUID, Operation> operations;
    private Conciliation conciliation;
    private static final int maxOperationsToPoll = 10;

    @BeforeEach
    public void setUp(){
        operations = new HashMap<>();
        conciliation = this.conciliation = mock(Conciliation.class);
        outbox = new OutboxMemory(operations, this.conciliation, maxOperationsToPoll);
        operation = new Operation(UUID.randomUUID(), BigDecimal.TEN);
    }

    @Test
    public void sendAnOperation() {
        when(conciliation.send(operation)).thenReturn(true);
        outbox.send(operation);

        assertThat(operations.containsValue(operation), is(true));
        assertThat(operation.getCurrentState(), is(State.SENT));
    }

    @Test
    public void errorOnSendingAnOperation(){
        when(conciliation.send(operation)).thenReturn(false);
        outbox.send(operation);

        assertThat(operations.containsValue(operation), is(true));
        assertThat(operation.getCurrentState(), is(State.ERROR));
    }

    @Test
    public void whenOutboxIsEmptyItDoesNotSendUnsentOperations(){
        outbox.sendUnsentOperations();
        
        verify(conciliation, never()).send(any(Operation.class));
    }

    @Test
    public void whenOutboxContainsOnlySentOperationsItDoesNotSendAnyOperation(){
        operation.markAsSending();
        operation.markAsSent();
        operations.put(operation.getCID(), operation);

        outbox.sendUnsentOperations();

        verify(conciliation, never()).send(any(Operation.class));
    }

    @Test
    public void sendUnsentOperations(){

        outbox.sendUnsentOperations();

        throw new UnsupportedOperationException("Não implementado");
    }

    @Test
    public void getANumberOfOperationsEqualsToTheMaximum(){
        outbox.sendUnsentOperations();

        throw new UnsupportedOperationException("Não implementado");
    }
}

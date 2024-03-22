package org.smartregister.chw.lab.pojo;

import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;

import androidx.annotation.NonNull;

public class CdpOutletEventClient {


    private Event event;
    private Client client;

    public CdpOutletEventClient(@NonNull Client client, @NonNull Event event) {
        this.client = client;
        this.event = event;
    }

    @NonNull
    public Client getClient() {
        return client;
    }

    @NonNull
    public Event getEvent() {
        return event;
    }
}

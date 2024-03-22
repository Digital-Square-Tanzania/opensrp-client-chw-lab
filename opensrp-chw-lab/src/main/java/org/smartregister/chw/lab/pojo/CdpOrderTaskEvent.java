package org.smartregister.chw.lab.pojo;


import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Task;

public class CdpOrderTaskEvent {
    private final Event event;
    private final Task task;

    public CdpOrderTaskEvent(Event event, Task task){
        this.event = event;
        this.task = task;
    }

    public Event getEvent() {
        return event;
    }

    public Task getTask() {
        return task;
    }
}

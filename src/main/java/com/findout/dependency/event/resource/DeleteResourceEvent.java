package com.findout.dependency.event.resource;

import com.findout.dependency.event.DeleteEvent;

/**
 * Created by magnusdep on 14/03/14.
 */
public class DeleteResourceEvent implements DeleteEvent {
    private final Long id;

    public DeleteResourceEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package com.findout.dependency.event.resource;

import com.findout.dependency.event.UpdateEvent;

/**
 * Created by magnusdep on 17/03/14.
 */
public class UpdateResourceEvent implements UpdateEvent {
    private final Long id;
    private final ResourceDetails details;

    public UpdateResourceEvent(Long id, ResourceDetails details) {
        this.id = id;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public ResourceDetails getDetails() {
        return details;
    }
}

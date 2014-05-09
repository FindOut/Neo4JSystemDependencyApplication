package com.findout.dependency.event.resource;

import com.findout.dependency.event.CreatedEvent;

/**
 * Created by magnusdep on 05/03/14.
 */
public class ResourceCreatedEvent implements CreatedEvent {
    final Long id;
    final ResourceDetails details;

    public ResourceCreatedEvent(Long id, ResourceDetails details) {
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

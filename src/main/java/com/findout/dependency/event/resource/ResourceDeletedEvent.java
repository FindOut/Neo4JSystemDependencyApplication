package com.findout.dependency.event.resource;

import com.findout.dependency.event.DeletedEvent;

/**
 * Created by magnusdep on 14/03/14.
 */
public class ResourceDeletedEvent extends DeletedEvent {
    private Long id;
    private ResourceDetails details;
    private boolean deletionComplete;

    public ResourceDeletedEvent(Long id) {
        this.id = id;
    }

    public ResourceDeletedEvent(Long id, ResourceDetails details) {
        this.id = id;
        this.details = details;
        deletionComplete = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResourceDetails getDetails() {
        return details;
    }

    public void setDetails(ResourceDetails details) {
        this.details = details;
    }

    public boolean isDeletionComplete() {
        return deletionComplete;
    }

    public void setDeletionComplete(boolean deletionComplete) {
        this.deletionComplete = deletionComplete;
    }

    public static ResourceDeletedEvent deletionForbidden(Long id, ResourceDetails details) {
        ResourceDeletedEvent event = new ResourceDeletedEvent(id, details);
        event.setDeletionComplete(false);
        event.setEntityFound(true);
        return event;
    }

    public static ResourceDeletedEvent notFound(Long id){
        ResourceDeletedEvent event = new ResourceDeletedEvent(id);
        event.setEntityFound(false);
        return event;
    }
}

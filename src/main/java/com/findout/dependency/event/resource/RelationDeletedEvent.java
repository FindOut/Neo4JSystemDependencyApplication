package com.findout.dependency.event.resource;

import com.findout.dependency.event.DeletedEvent;

/**
 * Created by magnusdep on 21/03/14.
 */
public class RelationDeletedEvent extends DeletedEvent {

    private final Long resourceId;
    private final Long dependsOnId;
    private ResourceDetails details;
    private boolean deletionComplete;

    public RelationDeletedEvent(Long resourceId, Long dependsOnId) {
        this.resourceId = resourceId;
        this.dependsOnId = dependsOnId;
    }

    public RelationDeletedEvent(Long resourceId, Long dependsOnId, ResourceDetails details) {
        this.resourceId = resourceId;
        this.dependsOnId = dependsOnId;
        this.details = details;
        deletionComplete = true;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Long getDependsOnId() {
        return dependsOnId;
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

    public static RelationDeletedEvent deletionForbidden(Long id, Long dependsOnId, ResourceDetails details) {
        RelationDeletedEvent event = new RelationDeletedEvent(id, dependsOnId, details);
        event.setDeletionComplete(false);
        event.setEntityFound(true);
        return event;
    }

    public static RelationDeletedEvent notFound(Long resourceId, Long dependsOnId){
        RelationDeletedEvent event = new RelationDeletedEvent(resourceId, dependsOnId);
        event.setEntityFound(false);
        return event;
    }
}

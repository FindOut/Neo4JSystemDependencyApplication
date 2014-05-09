package com.findout.dependency.event.resource;

import com.findout.dependency.event.UpdatedEvent;

/**
 * Created by magnusdep on 18/03/14.
 *
 */
public class ResourcesConnectedEvent extends UpdatedEvent {
    private final Long resourceId;
    private final ResourceDetails resourceDetails;
    private final Long dependsOnId;

    public ResourcesConnectedEvent(Long resourceId, ResourceDetails resourceDetails, Long dependsOnId) {
        this.resourceId = resourceId;
        this.resourceDetails = resourceDetails;
        this.dependsOnId = dependsOnId;
    }

    public ResourcesConnectedEvent(Long dependsOnId, Long resourceId) {
        this.dependsOnId = dependsOnId;
        this.resourceId = resourceId;
        this.resourceDetails = null;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Long getDependsOnId() {
        return dependsOnId;
    }

    public ResourceDetails getResourceDetails() {
        return resourceDetails;
    }

    public static ResourcesConnectedEvent notFound(Long resourceId, Long dependsOnId){
        ResourcesConnectedEvent event = new ResourcesConnectedEvent(resourceId, dependsOnId);
        event.setEntityFound(false);
        return event;
    }
}

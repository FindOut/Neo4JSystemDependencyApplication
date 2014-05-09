package com.findout.dependency.service;

import com.findout.dependency.event.resource.*;

/**
 * Created by magnusdep on 10/03/14.
 */
public interface ResourceRestService {
    public ResourceDetailsEvent requestResourceDetails(RequestResourceDetailsEvent requestResourceDetailsEvent);
    public ResourceDetailsEvent findByName(FindResourceByNameEvent findResourceByNameEvent);
    public AllResourcesEvent findAll(RequestAllResourcesEvent requestAllResourcesEvent);
    public ResourceCreatedEvent addResource(CreateResourceEvent createResourceEvent);
    public ResourceDeletedEvent deleteResource(DeleteResourceEvent deleteResourceEvent);
    public RelationDeletedEvent deleteRelation(DeleteRelationEvent deleteRelationEvent);
    public ResourceUpdatedEvent updateResource(UpdateResourceEvent updateResourceEvent);
    public ResourcesConnectedEvent connectResources(ConnectResourcesEvent connectResourcesEvent);
}

package com.findout.dependency.rest.controller.fixture;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.resource.*;

import java.util.ArrayList;
import java.util.Collection;

import static com.findout.dependency.rest.controller.fixture.RestDataFixture.*;

/**
 * Created by magnusdep on 05/03/14.
 *
 */
public class RestEventFixtures {

    public static AllResourcesEvent allResourcesEvent(int nofResults){
        Collection<ResourceDetails> results = new ArrayList<>();
        for (int i = 0; i < nofResults; i++) {
            results.add(customResourceDetails(RestDataFixture.ID_1 + i));
        }
        return new AllResourcesEvent(results);
    }

    public static <T> ResourceDetailsEvent resourceDetailsNotFound(T id){
        return ResourceDetailsEvent.notFound(id);
    }

    public static ResourceDetailsEvent<String> resourceDetailsEvent(String name) {
        return new ResourceDetailsEvent<>(name, customResourceDetails(name));
    }

    public static ResourceDetailsEvent<Long> resourceDetailsEvent(Long id) {
        return new ResourceDetailsEvent<>(id, customResourceDetails(id));
    }

    public static ResourceCreatedEvent resourceCreated(Long id) {
        return new ResourceCreatedEvent(id, customResourceDetails(id));
    }

    public static ResourceUpdatedEvent resourceUpdated(Long id) {
        return new ResourceUpdatedEvent(id, customResourceDetails(id));
    }

    public static ResourceUpdatedEvent resourceUpdatedNotFound(Long id) {
        return ResourceUpdatedEvent.notFound(id);
    }

    public static ResourceDeletedEvent resourceDeleted(Long id) {
        return new ResourceDeletedEvent(id, standardResourceDetails());
    }

    public static ResourceDeletedEvent resourceDeletedFailed(Long id) {
        return  ResourceDeletedEvent.deletionForbidden(id, standardResourceDetails());
    }

    public static ResourceDeletedEvent resourceDeletedNotFound(Long id) {
        return ResourceDeletedEvent.notFound(id);
    }

    public static RelationDeletedEvent relationDeleted(Long id, Long dependsOn){
        return new RelationDeletedEvent(id, dependsOn, customResourceDetails(id));
    }

    public static RelationDeletedEvent relationDeletedFailed(Long id, Long dependsOn){
        return RelationDeletedEvent.deletionForbidden(id, dependsOn, customResourceDetails(id));
    }

    public static RelationDeletedEvent relationDeletedNotFound(Long id, Long dependsOn){
        return RelationDeletedEvent.notFound(id, dependsOn);
    }

    public static ResourcesConnectedEvent resourceConnected(Long id, Long dependsOnId, DependencyType type){
        return new ResourcesConnectedEvent(id, customResourceDetails(id, dependsOnId, type), dependsOnId);
    }

    public static ResourcesConnectedEvent resourceConnectedNotFound(Long id, Long dependsOnId){
        return ResourcesConnectedEvent.notFound(id, dependsOnId);
    }
}

package com.findout.dependency.rest.controller;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.resource.*;
import com.findout.dependency.rest.domain.RestResource;
import com.findout.dependency.service.ResourceRestService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by magnusdep on 05/03/14.
 */
@Api(value = "Write", description = "Write resources and their relations")
@RestController
@RequestMapping("/write/resources")
public class ResourceCommandController {
    @Autowired
    ResourceRestService resourceRestService;

    @RequestMapping(method = RequestMethod.POST)
    public RestResource createResource(@RequestBody RestResource restResource,
                                                       UriComponentsBuilder uriComponentsBuilder,
                                                       HttpServletResponse response){
        ResourceCreatedEvent resourceCreatedEvent = resourceRestService
                .addResource(new CreateResourceEvent(RestResource.toResourceDetails(restResource)));

        RestResource createdResource = RestResource.fromResourceDetails(resourceCreatedEvent.getDetails());

        response.setHeader("Location", uriComponentsBuilder.path("/read/resources/{id}")
                .buildAndExpand(resourceCreatedEvent.getId().toString()).toUri().toString());
        response.setStatus(HttpStatus.CREATED.value());

        return createdResource;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public RestResource deleteResource(@PathVariable Long id,
                                       HttpServletResponse response) {
        ResourceDeletedEvent resourceDeletedEvent = resourceRestService.deleteResource(
                new DeleteResourceEvent(id));

        if (!resourceDeletedEvent.isEntityFound()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        RestResource result = RestResource.fromResourceDetails(resourceDeletedEvent.getDetails());

        if (resourceDeletedEvent.isDeletionComplete()) {
            response.setStatus(HttpStatus.OK.value());
            return result;
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/relation/{id}")
    public RestResource deleteRelation(
            @PathVariable Long id,
            @RequestParam(value = "dependsOn", required = true) Long dependsOn,
            @RequestParam(value = "type", required = true) DependencyType type,
            HttpServletResponse response) {

        RelationDeletedEvent relationDeletedEvent = resourceRestService.deleteRelation(
                new DeleteRelationEvent(id, dependsOn, type));

        if (!relationDeletedEvent.isEntityFound()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        RestResource result = RestResource.fromResourceDetails(relationDeletedEvent.getDetails());

        if (relationDeletedEvent.isDeletionComplete()) {
            response.setStatus(HttpStatus.OK.value());
            return result;
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        return result;

    }

    @RequestMapping(method = RequestMethod.PUT)
    public RestResource updateResource(@RequestBody RestResource restResource,
                                                       UriComponentsBuilder builder,
                                                       HttpServletResponse response) {
        ResourceDetails details = RestResource.toResourceDetails(restResource);
        //if no id, create, otherwise update
        if (details.getId() == null || details.getId() <= 0) {
            return createResource(restResource, builder, response);
        }

        //update instead
        ResourceUpdatedEvent updatedEvent = resourceRestService.updateResource(
            new UpdateResourceEvent(details.getId(), details)
        );

        if (!updatedEvent.isEntityFound()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        response.setHeader("Location", builder.path("/read/resources/{id}")
                .buildAndExpand(updatedEvent.getId().toString()).toUri().toString());
        response.setStatus(HttpStatus.OK.value());

        return RestResource.fromResourceDetails(updatedEvent.getDetails());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public RestResource addDependency(
            @PathVariable Long id,
            @RequestParam(value = "dependsOn", required = true) Long dependsOn,
            @RequestParam(value = "type", required = true) DependencyType type,
                                                      UriComponentsBuilder builder,
                                                      HttpServletResponse response){

        ResourcesConnectedEvent event = resourceRestService.connectResources(
                new ConnectResourcesEvent(id, dependsOn, type));
        if (!event.isEntityFound()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        response.setHeader("Location", builder.path("/read/resources/{id}")
                .buildAndExpand(event.getResourceId().toString()).toUri().toString());
        response.setStatus(HttpStatus.OK.value());

        return RestResource.fromResourceDetails(event.getResourceDetails());
    }
}

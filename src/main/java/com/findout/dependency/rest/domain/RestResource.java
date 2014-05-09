package com.findout.dependency.rest.domain;

import com.findout.dependency.event.resource.ResourceDetails;
import com.findout.dependency.rest.controller.ResourceQueryController;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Use of {@link javax.xml.bind.annotation.XmlRootElement} to enable rest response using XML.
 * Extending {@link org.springframework.hateoas.ResourceSupport} to enable HATEOAS.
 */
@XmlRootElement
public class RestResource extends ResourceSupport implements Serializable{
    String resourceId;
    String name;
    String description;

    public RestResource() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static RestResource fromResourceDetails(ResourceDetails details) {
        RestResource restResource = new RestResource();
        restResource.setResourceId(details.getId().toString());
        restResource.setName(details.getName());
        restResource.setDescription(details.getDescription());

        restResource.add(linkTo(ResourceQueryController.class).slash(restResource.getResourceId()).withSelfRel());

        for (Long id: details.getDependencies().keySet()){
            restResource.add(linkTo(ResourceQueryController.class)
                    .slash(id.toString()).withRel(details.getDependencies().get(id).name()));
        }

        return  restResource;
    }

    public static ResourceDetails toResourceDetails(RestResource restResource){
        ResourceDetails details = new ResourceDetails();
        if (restResource.getResourceId() != null && restResource.getResourceId().length() > 0) {
            details.setId(Long.parseLong(restResource.getResourceId()));
        }
        details.setName(restResource.getName());
        details.setDescription(restResource.getDescription());
        return details;
    }
}

package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.*;
import com.findout.dependency.rest.domain.RestResource;
import com.findout.dependency.rest.domain.RestResourceList;
import com.findout.dependency.service.ResourceRestService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magnusdep on 05/03/14.
 *
 */
@Api(value = "Read", description = "Read resources and their relations")
@RestController
@RequestMapping("/read/resources")
public class ResourceQueryController {
    @Autowired
    ResourceRestService resourceRestService;

    @ApiOperation(value = "Get a specific Resource", response = RestResource.class,
            notes = "Retrieves a specific resource based on numeric id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Resource found",response = RestResource.class),
            @ApiResponse(code = 404, message = "No resource found with the provided id")
    })
    @RequestMapping(method = RequestMethod.GET , value = "/{id}")
    public RestResource getResourceDetails(@PathVariable Long id,
                                           HttpServletResponse response) {
        ResourceDetailsEvent detailsEvent = resourceRestService.requestResourceDetails(new RequestResourceDetailsEvent(id));

        if (!detailsEvent.isEntityFound()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        response.setStatus(HttpStatus.OK.value());

        return RestResource.fromResourceDetails(detailsEvent.getResourceDetails());
    }

    @RequestMapping(method = RequestMethod.GET , value = "/getByName/{name}")
    public RestResource getResourceDetails(@PathVariable String name,
                                           HttpServletResponse response) {
        ResourceDetailsEvent detailsEvent = resourceRestService.findByName(new FindResourceByNameEvent(name));

        if (!detailsEvent.isEntityFound()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        response.setStatus(HttpStatus.OK.value());

        return RestResource.fromResourceDetails(detailsEvent.getResourceDetails());
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public RestResourceList getAllResources(HttpServletResponse response) {
        List<RestResource> result = new ArrayList<>();
        for (ResourceDetails detail: resourceRestService.findAll(new RequestAllResourcesEvent()).getResourceDetails()) {
            result.add(RestResource.fromResourceDetails(detail));
        }
        response.setStatus(HttpStatus.OK.value());
        return new RestResourceList(result);
    }
}

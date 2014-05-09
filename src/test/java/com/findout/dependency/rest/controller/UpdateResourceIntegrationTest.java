package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.CreateResourceEvent;
import com.findout.dependency.event.resource.UpdateResourceEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.findout.dependency.rest.controller.fixture.RestDataFixture.*;
import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by magnusdep on 17/03/14.
 **/

@Test(groups = "integration")
public class UpdateResourceIntegrationTest {
    @InjectMocks
    ResourceCommandController controller;

    @Mock
    ResourceRestService service;

    @BeforeMethod
    public void setup(){
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(controller);
    }

    @Test
    public void testThatUpdateResourceUsesHttpCreatedWhenCreate() throws Exception {
        when(service.addResource(any(CreateResourceEvent.class)))
                .thenReturn(resourceCreated(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

        verify(service).addResource(argThat(
            new ResourceCreateMatcher(RestDataFixture.NAME_1, RestDataFixture.DESCRIPTION_1)
        ));
    }

    @Test
    public void testThatUpdateResourcePassesLocationHeaderWhenCreate() throws Exception {
        when(service.addResource(any(CreateResourceEvent.class)))
                .thenReturn(resourceCreated(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .header("Location", Matchers.endsWith("read/resources/" + RestDataFixture.ID_1));
    }

    @Test
    public void testThatUpdateResourcePassesLocationHeaderWhenUpdate() throws Exception {
        when(service.updateResource(any(UpdateResourceEvent.class)))
                .thenReturn(resourceUpdated(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(standardResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .header("Location", Matchers.endsWith("read/resources/" + RestDataFixture.ID_1));
    }

    class ResourceCreateMatcher extends ArgumentMatcher<CreateResourceEvent>{
        final String name;
        final String description;

        ResourceCreateMatcher(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public boolean matches(Object item) {
            CreateResourceEvent event = (CreateResourceEvent)item;
            return event.getDetails().getName().equals(name)
                    && event.getDetails().getDescription().equals(description);
        }
    }

    @Test
    public void testThatUpdateResourceUsesHttpOkWhenUpdate() throws Exception {
        when(service.updateResource(any(UpdateResourceEvent.class)))
                .thenReturn(resourceUpdated(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(standardResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        verify(service).updateResource(argThat(
                new ResourceUpdateMatcher(RestDataFixture.ID_1,
                        RestDataFixture.NAME_1,
                        RestDataFixture.DESCRIPTION_1)
        ));
    }

    class ResourceUpdateMatcher extends ArgumentMatcher<UpdateResourceEvent>{
        final Long id;
        final String name;
        final String description;

        ResourceUpdateMatcher(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public boolean matches(Object item) {
            UpdateResourceEvent event = (UpdateResourceEvent)item;
            return event.getId().equals(id)
                    && event.getDetails().getName().equals(name)
                    && event.getDetails().getDescription().equals(description);
        }
    }
    @Test
    public void testThatCreateResourceRendersAsJson() throws Exception {
        when(service.updateResource(any(UpdateResourceEvent.class)))
                .thenReturn(resourceUpdated(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(standardResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("resourceId", equalTo(RestDataFixture.ID_1.toString()))
                .body("name", equalTo(RestDataFixture.NAME_1))
                .body("description", equalTo(RestDataFixture.DESCRIPTION_1));
    }


    @Test
    public void testThatUpdateResourceUsesHttpNotFoundOnEntityLookupFailure() throws Exception{
        when(service.updateResource(any(UpdateResourceEvent.class)))
                .thenReturn(resourceUpdatedNotFound(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(standardResourceJSON())
                .when()
                .put("/write/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(service).updateResource(argThat(
                Matchers.<UpdateResourceEvent>hasProperty("id",
                        Matchers.equalTo(RestDataFixture.ID_1))));
    }
}

package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.DeleteResourceEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by magnusdep on 13/03/14.
 */
@Test(groups = "integration")
public class DeleteResourceIntegrationTest {
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
    public void testThatDeleteResourceUsesHttpOkOnSuccess() throws Exception{
        when(service.deleteResource(any(DeleteResourceEvent.class)))
                .thenReturn(resourceDeleted(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .when()
                .delete("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        verify(service).deleteResource(argThat(
                Matchers.<DeleteResourceEvent>hasProperty("id",
                        Matchers.equalTo(RestDataFixture.ID_1))));
    }

    @Test
    public void testThatDeleteResourceUsesHttpNotFoundOnEntityLookupFailure() throws Exception{
        when(service.deleteResource(any(DeleteResourceEvent.class)))
                .thenReturn(resourceDeletedNotFound(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .when()
                .delete("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(service).deleteResource(argThat(
                Matchers.<DeleteResourceEvent>hasProperty("id",
                        Matchers.equalTo(RestDataFixture.ID_1))));
    }

    @Test
    public void testThatDeleteResourceUsesHttpForbiddenOnDeletionFailures() throws Exception {
        when(service.deleteResource(any(DeleteResourceEvent.class)))
                .thenReturn(resourceDeletedFailed(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .when()
                .delete("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());

        verify(service).deleteResource(argThat(
                Matchers.<DeleteResourceEvent>hasProperty("id",
                        Matchers.equalTo(RestDataFixture.ID_1))));
    }
}

package com.findout.dependency.rest.controller;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.resource.ConnectResourcesEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.rest.domain.RestResource;
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

import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by magnusdep on 19/03/14.
 *
 */
@Test(groups = "integration")
public class AddDependencyIntegrationTest {
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
    public void testThatConnectResourcesUsesHttpOkWhenConnected() throws Exception {
        when(service.connectResources(any(ConnectResourcesEvent.class)))
                .thenReturn(resourceConnected(RestDataFixture.ID_1, RestDataFixture.ID_2, DependencyType.FILE));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        verify(service).connectResources(argThat(
                new AddDependencyMatcher(RestDataFixture.ID_1,
                        RestDataFixture.ID_2,
                        DependencyType.FILE)
        ));
    }

    @Test
    public void testThatConnectResourcesUsesHttpNotFoundOnEntityLookupFailure() throws Exception{
        when(service.connectResources(any(ConnectResourcesEvent.class)))
                .thenReturn(resourceConnectedNotFound(RestDataFixture.ID_1, RestDataFixture.ID_2));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(service).connectResources(argThat(
                new AddDependencyMatcher(RestDataFixture.ID_1,
                        RestDataFixture.ID_2,
                        DependencyType.FILE)
        ));
    }

    @Test
    public void testThatConnectResourcesUsesHttpBadRequestOnMissingType() throws Exception{

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("type", DependencyType.FILE)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testThatConnectResourcesUsesHttpBadRequestOnMissingDependsOn() throws Exception{

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testThatConnectResourcesPassesLocationHeaderWhenConnected() throws Exception {
        when(service.connectResources(any(ConnectResourcesEvent.class)))
                .thenReturn(resourceConnected(RestDataFixture.ID_1, RestDataFixture.ID_2, DependencyType.FILE));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .header("Location", Matchers.endsWith("read/resources/" + RestDataFixture.ID_1));
    }

    @Test
    public void testThatConnectResourcesHasCorrectHATEOASLinksWhenConnected() throws Exception {
        when(service.connectResources(any(ConnectResourcesEvent.class)))
                .thenReturn(resourceConnected(RestDataFixture.ID_1, RestDataFixture.ID_2, DependencyType.FILE));

        RestResource res = given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .put("/write/resources/{id}", RestDataFixture.ID_1)
                .as(RestResource.class);

        String resourceBase = "read/resources/";

        assertNotNull(res);
        assertTrue(res.getId().getHref().endsWith(resourceBase + RestDataFixture.ID_1));
        assertNotNull(res.getLink(DependencyType.FILE.name()));
        assertTrue(res.getLink(DependencyType.FILE.name()).getHref().endsWith(resourceBase + RestDataFixture.ID_2));
    }



    class AddDependencyMatcher extends ArgumentMatcher<ConnectResourcesEvent> {
        final Long id;
        final Long dependsOnId;
        final DependencyType type;

        AddDependencyMatcher(Long id, Long dependsOnId, DependencyType type) {
            this.id = id;
            this.dependsOnId = dependsOnId;
            this.type = type;
        }

        @Override
        public boolean matches(Object item) {
            ConnectResourcesEvent event = (ConnectResourcesEvent)item;
            return event.getResourceId().equals(id)
                    && event.getDependsOnId().equals(dependsOnId)
                    && event.getType().equals(type);
        }
    }
}

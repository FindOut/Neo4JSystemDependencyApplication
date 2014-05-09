package com.findout.dependency.rest.controller;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.resource.DeleteRelationEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.mockito.ArgumentMatcher;
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
 * Created by magnusdep on 21/03/14.
 *
 */
@Test(groups = "integration")
public class DeleteRelationIntegrationTest {
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
    public void testThatDeleteRelationUsesHttpOkOnSuccess() throws Exception{
        when(service.deleteRelation(any(DeleteRelationEvent.class)))
                .thenReturn(relationDeleted(RestDataFixture.ID_1, RestDataFixture.ID_2));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .delete("/write/resources/relation/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        verify(service).deleteRelation(argThat(
                new RemoveDependencyMatcher(RestDataFixture.ID_1,
                        RestDataFixture.ID_2,
                        DependencyType.FILE)
        ));
    }

    @Test
    public void testThatDeleteRelationUsesHttpNotFoundOnEntityLookupFailure() throws Exception{
        when(service.deleteRelation(any(DeleteRelationEvent.class)))
                .thenReturn(relationDeletedNotFound(RestDataFixture.ID_1, RestDataFixture.ID_2));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .delete("/write/resources/relation/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(service).deleteRelation(argThat(
                new RemoveDependencyMatcher(RestDataFixture.ID_1,
                        RestDataFixture.ID_2,
                        DependencyType.FILE)
        ));
    }

    @Test
    public void testThatDeleteResourceUsesHttpForbiddenOnDeletionFailures() throws Exception {
        when(service.deleteRelation(any(DeleteRelationEvent.class)))
                .thenReturn(relationDeletedFailed(RestDataFixture.ID_1, RestDataFixture.ID_2));

        given()
                .header("Accept", ContentType.JSON)
                .queryParam("dependsOn", RestDataFixture.ID_2)
                .queryParam("type", DependencyType.FILE)
                .when()
                .delete("/write/resources/relation/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());

        verify(service).deleteRelation(argThat(
                new RemoveDependencyMatcher(RestDataFixture.ID_1,
                        RestDataFixture.ID_2,
                        DependencyType.FILE)
        ));
    }

    class RemoveDependencyMatcher extends ArgumentMatcher<DeleteRelationEvent> {
        final Long id;
        final Long dependsOnId;
        final DependencyType type;

        RemoveDependencyMatcher(Long id, Long dependsOnId, DependencyType type) {
            this.id = id;
            this.dependsOnId = dependsOnId;
            this.type = type;
        }

        @Override
        public boolean matches(Object item) {
            DeleteRelationEvent event = (DeleteRelationEvent)item;
            return event.getResourceId().equals(id)
                    && event.getDependsOnId().equals(dependsOnId)
                    && event.getType().equals(type);
        }
    }
}

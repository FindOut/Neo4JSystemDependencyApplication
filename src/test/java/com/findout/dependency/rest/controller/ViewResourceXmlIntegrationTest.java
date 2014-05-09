package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.RequestAllResourcesEvent;
import com.findout.dependency.event.resource.RequestResourceDetailsEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.rest.domain.RestResourceList;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.allResourcesEvent;
import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.resourceDetailsEvent;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by magnusdep on 10/03/14.
 */
@Test(groups = "integration")
public class ViewResourceXmlIntegrationTest {

    @InjectMocks
    ResourceQueryController controller;

    @Mock
    ResourceRestService service;

    @BeforeMethod
    public void setup(){
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.reset();
        RestAssuredMockMvc.standaloneSetup(controller);
    }

    @Test
    public void thatViewResourceByIdRendersCorrectlyAsXml() throws Exception {
        when(service.requestResourceDetails(any(RequestResourceDetailsEvent.class)))
                .thenReturn(resourceDetailsEvent(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.XML)
                .when()
                .get("/read/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.XML)
                .body("restResource.resourceId", equalTo(RestDataFixture.ID_1.toString()))
                .body("restResource.name", equalTo(RestDataFixture.NAME_1))
                .body("restResource.description", equalTo(RestDataFixture.DESCRIPTION_1));
    }

    @Test
    public void thatViewAllResourcesRendersCorrectlyAsXml() throws Exception {
        final int nofResults = 5;

        when(service.findAll(any(RequestAllResourcesEvent.class)))
                .thenReturn(allResourcesEvent(nofResults));

        //Deserializing xml to POJO.
        // rest-assured does not support List<T> so have to use [].
        RestResourceList result = given()
                .header("Accept", ContentType.XML)
                .when()
                .get("/read/resources")
                .as(RestResourceList.class);


        assertNotNull(result);
        assertEquals(result.getData().size(), 5);
        for (int i = 0; i < result.getData().size(); i++){
            assertEquals(result.getData().get(i).getResourceId(), String.valueOf(RestDataFixture.ID_1 + i));
            assertEquals(result.getData().get(i).getName(), RestDataFixture.NAME_1);
            assertEquals(result.getData().get(i).getDescription(), RestDataFixture.DESCRIPTION_1);
        }
    }

    @Test
    public void thatViewAllResourcesRendersCorrectlyAsXmlWithCorrectHeaders() throws Exception {
        final int nofResults = 5;

        when(service.findAll(any(RequestAllResourcesEvent.class)))
                .thenReturn(allResourcesEvent(nofResults));

        given()
                .header("Accept", MediaType.APPLICATION_XML_VALUE)
                .when()
                .get("/read/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.XML);

    }
}

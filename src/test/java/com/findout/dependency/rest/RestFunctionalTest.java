package com.findout.dependency.rest;

import com.findout.dependency.rest.domain.RestResourceList;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Needs to have application deployed on local jetty.
 */
@Test(groups = "functional")
public class RestFunctionalTest {

    @Test
    public void testThatResourcesCanBeQueried() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);

        ResponseEntity<RestResourceList> res = restTemplate.exchange("http://localhost:8080/read/resources",
                HttpMethod.GET, httpEntity, RestResourceList.class);

        assertNotNull(res);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertNotNull(res.getBody());
        assertTrue((res.getBody().getData().size() > 0));
    }

    @Test
    public void thatResourcesHaveCorrectHateoasLinks() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);

        ResponseEntity<RestResourceList> res = restTemplate.exchange("http://localhost:8080/read/resources",
                HttpMethod.GET, httpEntity, RestResourceList.class);

        String resourceBase = "/read/resources/" + res.getBody().getData().get(0).getResourceId();

        assertTrue(res.getBody().getData().get(0).getLink("self").getHref().endsWith(resourceBase));
    }

}

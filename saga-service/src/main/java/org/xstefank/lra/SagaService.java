package org.xstefank.lra;

import io.narayana.lra.client.NarayanaLRAClient;
import org.xstefank.lra.definition.rest.RESTAction;
import org.xstefank.lra.definition.rest.RESTLra;
import org.xstefank.lra.definition.rest.RESTLraBuilder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Stateless
public class SagaService {

    @Inject
    @CurrentLRAClient
    private NarayanaLRAClient lraClient;

    public String startSaga(String name, String uri) throws MalformedURLException, URISyntaxException {

        URL participant2Url = new URL("http://participant2-service:8080/api");

        RESTLra lra = RESTLraBuilder.lra()
                .name("testing saga")
                .data(42)
                .withAction(RESTAction.post(new URL("http://participant1-service:8080/api")).build())
                .withAction(RESTAction
                        .post(UriBuilder.fromUri(participant2Url.toURI()).path("/request").build().toURL())
                        .callbackUrl(participant2Url)
                        .build())
                .callback(uri)
                .build();

        lraClient.startLRA(lra);

        return "Invocation saga-sevice completed";
    }

}

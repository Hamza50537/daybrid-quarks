package de.hiqs.daybird.api.shared;

import de.hiqs.daybird.api.shared.domains.util.JsonUtils;
import de.hiqs.daybird.api.shared.keycloak.AccessTokenProvider;
import de.hiqs.daybird.api.shared.keycloak.KeycloakResource;
import io.quarkus.test.common.QuarkusTestResource;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@QuarkusTestResource(KeycloakResource.class)
public abstract class AbstractMockWebServerTest extends AccessTokenProvider {
    public static final int TEST_MOCK_WEB_SERVER_PORT = 7775;
    public static final String TEST_MOCK_WEB_SERVER_PORT_PROPERTY_KEY = "test.mock_web_server.port";
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMockWebServerTest.class);

    public static MockWebServer mockBackEnd;
    protected final JsonUtils jsonUtils = new JsonUtils();

    @BeforeAll
    public static void beforeAll() {
        //startMockWebServerRandomPort();
        // TODO: make it dynamically
        startMockWebServerFixPort(TEST_MOCK_WEB_SERVER_PORT);
    }

    @AfterAll
    public static void afterAll() {
        stopMockWebServer();
    }

    protected static void stopMockWebServer() {
        LOGGER.info("Stopping Mock WebServer ");
        try {
            mockBackEnd.shutdown();
            Awaitility.await().pollDelay(500, TimeUnit.MILLISECONDS).until(() -> true);
        } catch (IOException e) {
            LOGGER.error("Couldn't stop Mock WebServer", e);
            throw new RuntimeException(e);
        }
    }

    protected static void startMockWebServerRandomPort() {
        LOGGER.info("Starting  Mock WebServer on random Port");
        mockBackEnd = new MockWebServer();
        try {
            mockBackEnd.start();
        } catch (IOException e) {
            LOGGER.error("Couldn't start Mock WebServer on random port", e);
            throw new RuntimeException(e);
        }
    }

    protected static void startMockWebServerFixPort(int port) {
        LOGGER.info("Starting Mock WebServer on port {}", port);
        mockBackEnd = new MockWebServer();

        try {
            mockBackEnd.start(port);
        } catch (IOException e) {
            LOGGER.error(String.format("Couldn't start Mock WebServer on port %d", port), e);
            throw new RuntimeException(e);
        }
    }

    protected void setMockResponse(Object o) {
        String body = (o instanceof String ? o.toString() : jsonUtils.toJson(o));
        mockBackEnd.enqueue(new MockResponse().setBody(body)
                .addHeader("Content-Type", "application/json"));
    }

    protected void setMockResponse(Response.Status httpStatus, String message) {
        mockBackEnd.enqueue(new MockResponse().setResponseCode(httpStatus.getStatusCode()).setBody(message)
                .addHeader("Content-Type", "application/json"));
    }

    protected void setMockResponse(Dispatcher dispatcher) {
        mockBackEnd.setDispatcher(dispatcher);
    }

    protected String getMockServerBaseUri() {
        return "http://" + mockBackEnd.getHostName() + ":" + mockBackEnd.getPort();
    }

}


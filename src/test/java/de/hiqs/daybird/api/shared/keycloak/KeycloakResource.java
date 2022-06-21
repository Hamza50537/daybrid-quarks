package de.hiqs.daybird.api.shared.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class KeycloakResource implements QuarkusTestResourceLifecycleManager {

    KeycloakContainer keycloak;

    @Override
    public Map<String, String> start() {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:16.1.1")
                .withRealmImportFile("/daybird-quarkus-realm.json");
        keycloak.start();

        return Map.of("quarkus.oidc.auth-server-url", keycloak.getAuthServerUrl() + "/realms/daybird-quarkus",
                "quarkus.oidc-client.auth-server-url", keycloak.getAuthServerUrl() + "/realms/daybird-quarkus");
    }

    @Override
    public void stop() {
        if (keycloak != null) {
            keycloak.stop();
        }
    }
}

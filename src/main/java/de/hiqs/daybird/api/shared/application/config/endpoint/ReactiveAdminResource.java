package de.hiqs.daybird.api.shared.application.config.endpoint;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class ReactiveAdminResource {

    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String admin() {
        String jwtString = "Access for subject " + jwt.getSubject() + " is granted";

        return "Admin resource : " + jwtString;
    }
}


package de.hiqs;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public class UserResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user_access")
    public String hello() {
        return "User can see this";
    }
}

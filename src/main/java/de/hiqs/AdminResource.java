package de.hiqs;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class AdminResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin_access")
    public String hello() {
        return "Admin resource : ";
    }
}

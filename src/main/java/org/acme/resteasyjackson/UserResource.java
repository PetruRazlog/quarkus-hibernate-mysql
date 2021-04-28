package org.acme.resteasyjackson;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/users")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<User> users = User.listAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
    return User.findByIdOptional(id)
            .map(user -> Response.ok(user).build())
            .orElse(Response.status(404).build());
    }

    @GET
    @Path("fname/{fname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByFirstName(@PathParam("fname") String fname) {
        return User.find("fname", fname)
                .singleResultOptional()
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @GET
    @Path("lname/{lname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByLastName(@PathParam("lname") String lname) {
        return User.find("lname", lname)
                .singleResultOptional()
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        User.persist(user);
        if (user.isPersistent()) {
            return Response.created(URI.create("/users" + user.id)).build();
        }
        return Response.status(400).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public User updateFirstName(@PathParam("id") Long id, User user) {
        if (user.fname == null) {
            throw new WebApplicationException("User not set on request.", 422);
        }
        User req_user = User.findById(id);
        if (req_user == null) {
            throw new WebApplicationException("User do not exist.", 404);
        }
        req_user.fname = user.fname;
        req_user.lname = user.lname;
        return req_user;
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = User.deleteById(id);
        if (deleted) {
            return Response.status(204).build();
        }
        return Response.status(400).build();
    }
}

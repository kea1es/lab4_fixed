package services;

import beansLab.entities.Shot;
import beansLab.entities.User;
import resources.UserManager;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Path("/shots")
@Produces(MediaType.TEXT_PLAIN)
public class ShotResource {

    @EJB
    private UserManager ejb;

    @Context
    private HttpServletRequest request;

    @GET
    public Response getShots() {
        User user = (User) request.getSession().getAttribute("User");
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        String csv = user.getShots().stream()
                .sorted((s1, s2) -> s2.getStart().compareTo(s1.getStart()))
                .map(this::formatToCSV)
                .collect(Collectors.joining("\n"));

        return Response.ok(csv).header("rows", user.getShots().size()).build();
    }

    @POST
    public Response addShot(@FormParam("coord_x") Double x,
                            @FormParam("coord_y") Double y,
                            @FormParam("coord_r") Double r) {

        User user = (User) request.getSession().getAttribute("User");
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        if (x == null || y == null || r == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing coordinates").build();
        }

        Shot shot = ShotGenerator.generateShot(x, y, r);
        ejb.addShotToUser(user, shot);

        return Response.ok(formatToCSV(shot)).header("rows", user.getShots().size()).build();
    }

    @DELETE
    public Response clearShots() {
        User user = (User) request.getSession().getAttribute("User");
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        ejb.clearUser(user);
        return Response.ok("CLEARED").build();
    }

    private String formatToCSV(Shot shot) {
        return shot.getX() + " " +
                shot.getY() + " " +
                shot.getR() + " " +
                shot.isRG() + " " +
                shot.getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy;HH:mm:ss")) + " " +
                (shot.getScriptTime() / 1000);
    }
}

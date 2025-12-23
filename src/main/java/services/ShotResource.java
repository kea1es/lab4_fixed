package services;

import beansLab.entities.Shot;
import beansLab.entities.User;
import resources.UserManager;
import dao.UserDao;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Path("/shots")
@Produces(MediaType.TEXT_PLAIN)
public class ShotResource {

    @EJB
    private UserManager ejb;

    @Context
    private HttpServletRequest request;

    private UserDao userDao = new UserDao();

    @GET
    public Response getShots() {
        String login = (String) request.getSession().getAttribute("user");

        User user = userDao.findUserWithShots(login);

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String csv = user.getShots().stream()
                .sorted((s1, s2) -> s2.getStart().compareTo(s1.getStart()))
                .map(this::formatToCSV)
                .collect(Collectors.joining("\n"));

        return Response.ok(csv)
                .header("rows", user.getShots().size())
                .build();
    }

    @POST
    public Response addShot(@FormParam("coord_x") Double x,
                            @FormParam("coord_y") Double y,
                            @FormParam("coord_r") Double r) {

        String login = (String) request.getSession().getAttribute("user");
        if (login == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (x == null || y == null || r == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing coordinates")
                    .build();
        }

        Shot shot = ShotGenerator.generateShot(x, y, r);

        User user = userDao.findUserByLogin(login);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        ejb.addShotToUser(user, shot);

        User updatedUser = userDao.findUserWithShots(login);
        if (updatedUser != null) {
            request.getSession().setAttribute("User", updatedUser);
        }

        return Response.ok(formatToCSV(shot))
                .header("rows", updatedUser != null ? updatedUser.getShots().size() : 0)
                .build();
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
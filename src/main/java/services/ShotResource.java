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
import java.util.List;
import java.util.stream.Collectors;

@Path("/shots")
@Produces(MediaType.TEXT_PLAIN)
public class ShotResource {

    @EJB
    private UserManager ejb;

    @Context
    private HttpServletRequest request;

    @EJB
    private UserDao userDao;

    @GET
    public Response getShots(@QueryParam("limit") @DefaultValue("50") int limit,
                             @QueryParam("offset") @DefaultValue("0") int offset) {
        String login = (String) request.getSession().getAttribute("user");

        if (login == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<Shot> shots = userDao.findShotsByUserWithPagination(login, offset, limit);
        long totalShots = userDao.countShotsByUser(login);

        String csv = shots.stream()
                .sorted((s1, s2) -> s2.getStart().compareTo(s1.getStart()))
                .map(this::formatToCSV)
                .collect(Collectors.joining("\n"));

        return Response.ok(csv)
                .header("rows", shots.size())
                .header("total", totalShots)
                .header("offset", offset)
                .header("limit", limit)
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

        ejb.addShotToUser(login, shot);

        long totalShots = userDao.countShotsByUser(login);

        return Response.ok(formatToCSV(shot))
                .header("rows", totalShots)
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
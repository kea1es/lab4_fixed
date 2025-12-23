package services;

import beansLab.entities.User;
import resources.UserInf;
import resources.UserManager;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class AuthResource {

    @EJB
    private UserManager ejb;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/login")
    public Response login(@FormParam("login") String login, @FormParam("password") String password) {
        int result = ejb.checkUser(login, password);

        switch (result) {
            case 0:
                HttpSession session = request.getSession(true);
                UserInf userInf = ejb.loginUser(login, password, session);

                session.setAttribute("user", login);

                // Создаем cookie с токеном для клиента
                NewCookie tokenCookie = new NewCookie("token", String.valueOf(userInf.getToken()), "/", null, null, -1, false);
                return Response.ok()
                        .header("StatusOfLogIn", 0)
                        .cookie(tokenCookie)
                        .build();
            case 1:
                return Response.status(Response.Status.UNAUTHORIZED).header("StatusOfLogIn", 1).build();
            case 2:
                return Response.status(Response.Status.UNAUTHORIZED).header("StatusOfLogIn", 2).build();
            default:
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/register")
    public Response register(@FormParam("login") String login, @FormParam("password") String password) {
        boolean res = ejb.addUser(new User(login, password));
        if (res) {
            return Response.ok().header("StatusOfRegistration", 0).build();
        } else {
            return Response.status(Response.Status.CONFLICT).header("StatusOfRegistration", 1).build();
        }
    }

    @POST
    @Path("/exit")
    public Response logout() {
        ejb.endSession(request.getSession());
        return Response.ok().header("result", 0).build();
    }
}
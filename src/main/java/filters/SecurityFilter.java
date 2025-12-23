package filters;

import resources.UserManager;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class SecurityFilter implements ContainerRequestFilter {

    @EJB
    private UserManager ejb;

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String path = context.getUriInfo().getPath();

        if (path.contains("auth/login") || path.contains("auth/register")) {
            return;
        }

        HttpSession session = servletRequest.getSession(false);
        Cookie tokenCookie = context.getCookies().get("token");

        if (session == null || tokenCookie == null) {
            abortWithUnauthorized(context);
            return;
        }

        javax.servlet.http.Cookie servletCookie =
                new javax.servlet.http.Cookie("token", tokenCookie.getValue());

        if (!ejb.hasSession(session, servletCookie)) {
            abortWithUnauthorized(context);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("User not authenticated")
                .build());
    }
}
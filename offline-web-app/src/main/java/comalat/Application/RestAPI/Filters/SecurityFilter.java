package comalat.Application.RestAPI.Filters;

import comalat.Application.Domain.ResponseMessage.ErrorMessage;
import java.util.List;
import java.util.StringTokenizer;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.sun.research.ws.wadl.HTTPMethods;
import comalat.Services.FileServices.AccessData;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * @author SyleSakis
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String AUTHORIZATION_PREFIX = "Basic ";
    // HTTP Methods
    private static final String DELETE_METHOD = HTTPMethods.DELETE.name();
    private static final String POST_METHOD = HTTPMethods.POST.name();
    private static final String PUT_METHOD = HTTPMethods.PUT.name();
    // PATH
    private static final String LOGIN_PREFIX = "login";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getUriInfo().getPath().contains(LOGIN_PREFIX)) {
            return;
        }

        if (requestContext.getMethod().equals(DELETE_METHOD)
                || requestContext.getMethod().equals(POST_METHOD)
                || requestContext.getMethod().equals(PUT_METHOD)) {

            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_KEY);
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();

                // check for validation USER PASS
                if (AccessData.compareData(username, password)) {
                    return;
                }
            }

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("You dont have permission for this action");
            errorMessage.setCode(Status.UNAUTHORIZED.getStatusCode());
            errorMessage.setDocumentation("");

            Response unauthorizedStatus = Response.status(Status.UNAUTHORIZED).entity(errorMessage).build();

            requestContext.abortWith(unauthorizedStatus);
        }
    }

}

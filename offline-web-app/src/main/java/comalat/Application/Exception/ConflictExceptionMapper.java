package comalat.Application.Exception;

import comalat.Application.Domain.ResponseMessage.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author SyleSakis
 */
@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Override
    public Response toResponse(ConflictException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Status.CONFLICT.getStatusCode(), null);
        return Response.status(Status.CONFLICT)
                .entity(errorMessage)
                .build();
    }
}
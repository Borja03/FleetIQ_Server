/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Omar
 */
/*
 *  to handle custom exceptions for CRUD operations.
 */
@Provider
public class CrudExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        if (ex instanceof CreateException) {
            // Handle CreateException
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Error Code: CREATE_ERROR\nMessage: " + ex.getMessage())
                            .build();
        } else if (ex instanceof SelectException) {
            // Handle SelectException
            return Response.status(Response.Status.NOT_FOUND)
                            .entity("Error Code: SELECT_ERROR\nMessage: " + ex.getMessage())
                            .build();
        } else if (ex instanceof UpdateException) {
            // Handle UpdateException
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Error Code: UPDATE_ERROR\nMessage: " + ex.getMessage())
                            .build();
        } else if (ex instanceof DeleteException) {
            // Handle DeleteException
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Error Code: DELETE_ERROR\nMessage: " + ex.getMessage())
                            .build();
        } else {
            // Handle any unexpected ex
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error Code: INTERNAL_ERROR\nMessage: An unexpected error occurred: " + ex.getMessage())
                            .build();
        }
    }
}


package exception;

/**
 *
 * @author omar
 */
public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException() {
        super();
    }

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}

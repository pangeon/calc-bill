package pl.cecherz.calcbill.exeptions;

public class HTTPError {
    private int code;
    private String message;
    private String cause;
    private String exceptionMessage;

    HTTPError(int code, String message) {
        this.code = code;
        this.message = message;
    }
    HTTPError(int code, String message, String cause, String exceptionMessage) {
        this.code = code;
        this.message = message;
        this.cause = cause;
        this.exceptionMessage = exceptionMessage;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getExceptionCause() {
        return cause;
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }
}

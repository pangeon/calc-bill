package pl.cecherz.calcbill.exeptions;

public class HTTPError {
    private int code;
    private String message;

    HTTPError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

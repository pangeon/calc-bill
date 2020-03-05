package pl.cecherz.calcbill.exeptions;

public class DataIntegrityException extends RuntimeException {
    private Integer entityId;
    private String cause;
    private String exceptionMessage;

    public DataIntegrityException(Integer entityId, String cause, String exceptionMessage) {
        this.entityId = entityId;
        this.cause = cause;
        this.exceptionMessage = exceptionMessage;
    }
    Integer getEntityId() {
        return entityId;
    }
    String getExceptionCause() {
        return cause;
    }
    String getExceptionMessage() {
        return exceptionMessage;
    }
}

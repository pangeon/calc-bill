package pl.cecherz.calcbill.exeptions;

public class EmptyFindResultException extends RuntimeException {
    private Integer entityId;
    private String findResult;

    public EmptyFindResultException(Integer entityId, String findResult) {
        this.entityId = entityId;
        this.findResult = findResult;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public String getFindResult() {
        return findResult;
    }
}

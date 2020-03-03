package pl.cecherz.calcbill.exeptions;

public class EntityEmptyContentException extends RuntimeException {
    private Integer entityId;

    public EntityEmptyContentException(Integer entityId) {
        this.entityId = entityId;
    }

    Integer getEntityId() {
        return entityId;
    }
}

package pl.cecherz.calcbill.exeptions;

public class EntityNotFoundException extends RuntimeException {
    private Integer entityId;

    public EntityNotFoundException(Integer entityId) {
        this.entityId = entityId;
    }
    Integer getEntityId() {
        return entityId;
    }
}

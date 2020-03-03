package pl.cecherz.calcbill.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.cecherz.calcbill.utils.MessageBuilder;

public class RestExceptionHandler {
    private MessageBuilder message = new MessageBuilder(RestExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected HTTPError enitityIdNotFound(EntityNotFoundException e) {
        Integer entityId = e.getEntityId();
        message.getInfo("IdNotFound()", entityId, "status", HttpStatus.NOT_FOUND);
        return new HTTPError(404, "Entity id [" + entityId + "] not exist in this collection !");
    }
    @ExceptionHandler(EntityEmptyContentException.class)
    @ResponseStatus(HttpStatus.OK)
    protected HTTPError isEntityEmpty(EntityEmptyContentException e) {
        Integer entityId = e.getEntityId();
        message.getInfo("isEntityEmpty()", entityId, "status", HttpStatus.OK);
        return new HTTPError(200, "Entity id [" + entityId + "] exist but is empty !");
    }
    @ExceptionHandler(EmptyFindResultException.class)
    @ResponseStatus(HttpStatus.OK)
    protected HTTPError isFindResultEmpty(EmptyFindResultException e) {
        Integer entityId = e.getEntityId();
        String findResult = e.getFindResult();
        message.getInfo("isEntityEmpty()", entityId, "status", HttpStatus.OK);
        return new HTTPError(200, "Entity id [" + entityId + "] exist. There are no matching set of results ["+ findResult + "]");
    }
}

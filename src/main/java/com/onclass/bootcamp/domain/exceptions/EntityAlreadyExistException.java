package com.onclass.bootcamp.domain.exceptions;

import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class EntityAlreadyExistException extends BusinessException {

    public EntityAlreadyExistException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }


}

package com.onclass.bootcamp.domain.exceptions;

import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class InvalidFormatParamException extends BusinessException {

    public InvalidFormatParamException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }


}

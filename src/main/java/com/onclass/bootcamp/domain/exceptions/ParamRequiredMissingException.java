package com.onclass.bootcamp.domain.exceptions;

import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class ParamRequiredMissingException extends BusinessException {

    public ParamRequiredMissingException(TechnicalMessage technicalMessage) {
        super(technicalMessage);
    }


}

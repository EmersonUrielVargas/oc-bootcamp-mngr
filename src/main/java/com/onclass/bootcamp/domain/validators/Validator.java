package com.onclass.bootcamp.domain.validators;

import com.onclass.bootcamp.domain.constants.Constants;
import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import com.onclass.bootcamp.domain.exceptions.InvalidFormatParamException;
import com.onclass.bootcamp.domain.exceptions.ParamRequiredMissingException;
import com.onclass.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

public class Validator {

	public static Mono<Bootcamp> validateBootcamp(Bootcamp bootcamp) {
        return Mono.just(bootcamp)
                .then(validateName(bootcamp))
                .then(validateDescription(bootcamp))
                .then(validateDuration(bootcamp))
                .then(validateListCapabilities(bootcamp))
                .then(validateStartDate(bootcamp));
    }

    private static Mono<Bootcamp> validateListCapabilities(Bootcamp bootcamp) {
        if (isNullOrEmpty(bootcamp.capabilities())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (bootcamp.capabilities().size() < Constants.BOOTCAMP_MIN_CAPABILITIES_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.LIST_CAPABILITIES_IS_TOO_SHORT));
        }
        if (bootcamp.capabilities().size() > Constants.BOOTCAMP_MAX_CAPABILITIES_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.LIST_CAPABILITIES_IS_TOO_LONG));
        }
        return Mono.just(bootcamp);
    }

    private static Mono<Bootcamp> validateName(Bootcamp bootcamp) {
        if (isNullOrEmpty(bootcamp.name())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (bootcamp.name().length()  > Constants.BOOTCAMP_NAME_MAX_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.BOOTCAMP_NAME_TOO_LONG));
        }
        return Mono.just(bootcamp);
    }

    private static Mono<Bootcamp> validateDescription(Bootcamp bootcamp) {
        if (isNullOrEmpty(bootcamp.description())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (bootcamp.description().length()  > Constants.BOOTCAMP_DESCRIPTION_MAX_SIZE) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.BOOTCAMP_DESCRIPTION_TOO_LONG));
        }
        return Mono.just(bootcamp);
    }

    private static Mono<Bootcamp> validateDuration(Bootcamp bootcamp) {
        if (isNullOrEmpty(bootcamp.duration())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (bootcamp.duration()  < Constants.BOOTCAMP_MIN_DURATION) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.DURATION_BOOTCAMP_NOT_ALLOWED));
        }
        return Mono.just(bootcamp);
    }

    private static Mono<Bootcamp> validateStartDate(Bootcamp bootcamp) {
        if (isNullOrEmpty(bootcamp.startDate())){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        if (bootcamp.startDate().isBefore(LocalDate.now())) {
            return Mono.error(new InvalidFormatParamException(TechnicalMessage.START_DATE_BOOTCAMP_NOT_ALLOWED));
        }
        return Mono.just(bootcamp);
    }

    public static <T> boolean isNullOrEmpty(T value){
        if (Objects.nonNull(value)){
            if (value instanceof String str) {
                return str.isBlank();
            }else {
                return false;
            }
        }
        return true;
	}
}

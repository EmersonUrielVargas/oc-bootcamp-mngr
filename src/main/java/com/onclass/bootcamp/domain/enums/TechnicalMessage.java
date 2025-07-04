package com.onclass.bootcamp.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500","Something went wrong, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Bad Parameters, please verify data", ""),
    MISSING_REQUIRED_PARAM("400", "Missing required parameters, please verify data ", ""),
    INVALID_MESSAGE_ID("404", "Invalid Message ID, please verify", "messageId"),
    BOOTCAMP_NAME_TOO_LONG("400", "The bootcamp name is too long", ""),
    BOOTCAMP_DESCRIPTION_TOO_LONG("400", "The bootcamp description is too long", ""),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),
    BOOTCAMP_CREATED("201", "Bootcamp created successful", ""),
    BOOTCAMP_ALREADY_EXISTS("400","The bootcamp already exist." ,"" ),
    CAPABILITIES_NOT_FOUND("404","Some of the capabilities to be register were not found, please verify data" ,"" ),
    ERROR_ASSIGN_CAPABILITIES("500","Something went wrong with the capacity adapter, please try again" ,"" ),
    LIST_CAPABILITIES_IS_TOO_SHORT("404","List of the capabilities must contain at least one" ,"" ),
    LIST_CAPABILITIES_IS_TOO_LONG("404","List of the capabilities exceeds the allowed limit" ,"" ),
    DURATION_BOOTCAMP_NOT_ALLOWED("404","The duration of bootcamp is not allowed" ,"" ),
    START_DATE_BOOTCAMP_NOT_ALLOWED("404","The bootcamp start date is not allowed" ,"" ),
    ERROR_CREATING_BOOTCAMP("500","An error occurred while creating the bootcamp" ,"" );

    private final String code;
    private final String message;
    private final String param;
}
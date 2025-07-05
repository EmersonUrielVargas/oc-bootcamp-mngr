package com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String NO_ADITIONAL_ERROR_DETAILS = "No additional error details";
    public final String LOG_START_ASSIGN_CAPABILITIES = "Starting assign capabilities {} in a bootcamp id: {}";
    public final String LOG_START_GET_CAPABILITIES_PAGINATION = "Starting get capabilities with parameters order: {}, page: {}, size: {}";
    public final String LOG_START_GET_CAPABILITIES_BY_BOOTCAMPS_IDS = "Starting get capabilities with bootcamps ids : {}";


    public final String CAPABILITY_MNGR_PATH_ASSIGN = "/assing";
    public final String CAPABILITY_MNGR_PATH_GET_BY_BOOTCAMPS = "/bootcamps";

    public final String QUERY_PARAM_ORDER_SORT = "sort";
    public final String QUERY_PARAM_PAGE = "page";
    public final String QUERY_PARAM_SIZE = "size";
    public final String QUERY_PARAM_BOOTCAMPSIDS = "bootcampsIds";



    public final String STRING_ERROR_BODY_DATA = "Error body: {}";
}

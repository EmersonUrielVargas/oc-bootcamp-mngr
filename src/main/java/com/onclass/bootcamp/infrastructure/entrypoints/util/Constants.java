package com.onclass.bootcamp.infrastructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String BOOTCAMP_ERROR = "Error on bootcamp - [ERROR]";
    public static final String BOOTCAMP_CREATED_RS_OK = "bootcamp created successfully";
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred";

    public static final String PATH_POST_BOOTCAMP = "/bootcamp";
    public static final String PATH_GET_PAGINATED_BOOTCAMPS = "/bootcamp/all";
    public static final String PATH_DELETE_BOOTCAMP = "/bootcamp/{id}";

    public final String QUERY_PARAM_ORDER_SORT = "sort";
    public final String QUERY_PARAM_ITEM_SORT = "parameter";
    public final String QUERY_PARAM_PAGE = "page";
    public final String QUERY_PARAM_SIZE = "size";
    public final String QUERY_PARAM_ID = "id";
    public final String DEFAULT_SIZE_PAGINATION = "10";
    public final String DEFAULT_PAGE_PAGINATION = "0";
}

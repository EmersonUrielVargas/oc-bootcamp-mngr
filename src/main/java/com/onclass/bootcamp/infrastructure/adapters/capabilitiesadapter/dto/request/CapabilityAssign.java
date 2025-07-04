package com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.dto.request;

import java.util.List;

public record CapabilityAssign(Long bootcampId, List<Long> capabilitiesIds) {
}

package com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("capability-mngr")
public class CapabilityMngrProperties {
    private String baseUrl;
    private String timeout;
}

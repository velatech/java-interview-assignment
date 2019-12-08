package com.phayo.interviewentry.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties(prefix = "binlist")
public class BinListApi {
    private String url;
}

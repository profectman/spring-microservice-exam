package com.jeeho.gateway.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class RouteFilterVo {
    /**
     * Filter Name
     */
    private String name;
    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}

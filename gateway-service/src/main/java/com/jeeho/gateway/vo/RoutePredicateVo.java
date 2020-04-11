package com.jeeho.gateway.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class RoutePredicateVo {
    /**
     * 断言对应的Name
     */
    private String name;
    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}

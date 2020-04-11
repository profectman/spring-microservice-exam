package com.jeeho.gateway.module;

import com.jeeho.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 路由信息
 */
@Data
public class Route extends BaseEntity<Route> {
    /**
     * 路由ID
     */
    @NotBlank(message = "路由ID不能为空")
    private String routeId;
    /**
     * 路由名称
     */
    private String routeName;
    /**
     * 断言
     */
    private String predicates;
    /**
     * 过滤器
     */
    private String filters;
    /**
     * URI
     */
    @NotBlank(message = "路由URI不能为空")
    private String uri;
    /**
     * 排序
     */
    private String sort;
    /**
     * 启用禁用
     */
    @NotBlank(message = "路由状态不能为空")
    private String status;
}

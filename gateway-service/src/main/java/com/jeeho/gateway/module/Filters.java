package com.jeeho.gateway.module;

import com.jeeho.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Filters extends BaseEntity<Filters> {
    /**
     * 路由ID
     */
    @NotBlank(message = "路由ID不能为空")
    private String routeId;
    /**
     * 过滤器名称
     */
    @NotBlank(message = "过滤器名称不能为空")
    private String name;
    /**
     * 路由参数
     */
    private String args;
}

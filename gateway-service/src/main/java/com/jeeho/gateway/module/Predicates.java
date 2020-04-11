package com.jeeho.gateway.module;

import com.jeeho.common.core.persistence.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Predicates extends BaseEntity<Predicates> {
    /**
     * 路由ID
     */
    @NotBlank(message = "路由ID不能为空")
    private String routeId;
    /**
     * 断言名称
     */
    @NotBlank(message = "断言名称不能为空")
    private String name;
    /**
     * 断言参数
     */
    private String args;
}

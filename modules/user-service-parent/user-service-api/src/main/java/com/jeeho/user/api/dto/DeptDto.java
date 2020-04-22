package com.jeeho.user.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeeho.common.core.persistence.TreeEntity;
import com.jeeho.user.api.module.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门dto
 *
 * @author tangyi
 * @date 2018-10-25 12:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptDto extends TreeEntity<DeptDto> {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门描述
     */
    private String deptDesc;

    /**
     * 部门负责人
     */
    private String deptLeader;

    /**
     * 父部门ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    public DeptDto(Dept dept) {
        this.setId(dept.getId());
        this.deptName = dept.getDeptName();
        this.deptDesc = dept.getDeptDesc();
        this.deptLeader = dept.getDeptLeader();
        this.parentId = dept.getParentId();
        this.sort = dept.getSort();
        this.setCreator(dept.getCreator());
        this.setCreateDate(dept.getCreateDate());
        this.setModifier(dept.getModifier());
        this.setModifyDate(dept.getModifyDate());
    }
}

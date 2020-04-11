package com.jeeho.common.core.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeho.common.core.constant.CommonConstant;
import com.jeeho.common.core.utils.DateUtils;
import com.jeeho.common.core.utils.IdGen;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entity属性
 * @param <T>
 */
@Data
@NoArgsConstructor
public class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long id;
    /**
     * 创建者
     */
    String creator;
    /**
     * 创建时间
     */
    Date createDate;
    /**
     * 更新者
     */
    String modifier;
    /**
     * 更新日期
     */
    Date modifyDate;

    Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;
    /**
     * 系统编号
     */
    String applicationCode;
    /**
     * 租户编号
     */
    String tenantCode;
    /**
     * 是否为新纪录
     */
    boolean isNewRecord;
    /**
     * 扩展字段
     */
    String ext;
    public BaseEntity(Long id){
        this();
        this.id = id;
    }
    /**
     * 是否为新纪录
     */
    public boolean idNewRecord(){
        return this.isNewRecord || this.getId() == null;
    }
    /**
     * 设置属性
     * @param userCode
     * @param applicationCode
     * @param tenantCode
     */
    public void setCommonValue(String userCode,String applicationCode,String tenantCode){
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        if (this.isNewRecord()){
            this.setId(IdGen.snowflakeId());
            this.setNewRecord(true);
            this.creator = userCode;
            this.createDate = currentDate;
        }
        this.modifier = userCode;
        this.modifyDate = currentDate;
        this.applicationCode = applicationCode;
        this.tenantCode = tenantCode;
        this.delFlag = 0;
    }
    /**
     * 置空属性
     */
    public void clearCommonValue(){
        this.creator = null;
        this.createDate = null;
        this.modifier = null;
        this.modifyDate = null;
        this.delFlag = null;
        this.applicationCode = null;
        this.tenantCode = null;
    }
}

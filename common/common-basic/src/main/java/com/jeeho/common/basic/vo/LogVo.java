package com.jeeho.common.basic.vo;

import com.jeeho.common.basic.model.Log;
import com.jeeho.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * logVo
 *
 * @author tangyi
 * @date 2019-01-05 17:07
 */
@Data
public class LogVo extends BaseEntity<LogVo> {

    private Log log;

    private String username;
}

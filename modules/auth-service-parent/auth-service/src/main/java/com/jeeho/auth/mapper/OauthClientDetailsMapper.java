package com.jeeho.auth.mapper;

import com.jeeho.auth.api.module.OauthClientDetails;
import com.jeeho.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * oauth2客户端Mapper
 */
@Mapper
public interface OauthClientDetailsMapper extends CrudMapper<OauthClientDetails> {

}

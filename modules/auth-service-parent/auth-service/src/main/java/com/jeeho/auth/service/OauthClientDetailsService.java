package com.jeeho.auth.service;

import com.jeeho.auth.api.module.OauthClientDetails;
import com.jeeho.auth.mapper.OauthClientDetailsMapper;
import com.jeeho.common.core.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class OauthClientDetailsService extends CrudService<OauthClientDetailsMapper, OauthClientDetails> {
}

package com.jeeho.gateway.mapper;

import com.jeeho.common.core.persistence.CrudMapper;
import com.jeeho.gateway.module.Route;
import org.apache.ibatis.annotations.Mapper;

/**
 * Route Mapper
 */
@Mapper
public interface RouteMapper extends CrudMapper<Route> {
}

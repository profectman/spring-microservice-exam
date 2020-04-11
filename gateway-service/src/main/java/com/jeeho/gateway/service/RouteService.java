package com.jeeho.gateway.service;

import com.jeeho.common.core.constant.CommonConstant;
import com.jeeho.common.core.expcetions.CommonException;
import com.jeeho.common.core.service.CrudService;
import com.jeeho.common.core.utils.JsonMapper;
import com.jeeho.gateway.constants.GatewayConstant;
import com.jeeho.gateway.mapper.RouteMapper;
import com.jeeho.gateway.module.Route;
import com.jeeho.gateway.vo.RouteFilterVo;
import com.jeeho.gateway.vo.RoutePredicateVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RouteService extends CrudService<RouteMapper, Route> {
    private final DynamicRouteService dynamicRouteService;

    private final RedisTemplate redisTemplate;

    @Override
    public int insert(Route entity) {
        int update;
        //检查参数是否为空
        if (StringUtils.isBlank(entity.getRouteId()))
            throw new CommonException("Service id is empty");
        //检查路由是否已经存在
        Route condition = new Route();
        condition.setRouteId(entity.getRouteId());
        List<Route> routes = this.findList(condition);
        if (CollectionUtils.isNotEmpty(routes))
            throw new CommonException("Service id is exits");
        entity.setCommonValue("", GatewayConstant.SYS_CODE, GatewayConstant.DEFAULT_TENANT_CODE);
        //执行插入操作并发布事件
        if ((update = this.dao.insert(entity)) > 0){
            dynamicRouteService.add(routeDefinition(entity));
        }
        return update;
    }

    /**
     * 更新路由
     *
     * @param route route
     * @return int
     */
    @Override
    public int update(Route route) {
        int update;
        if (StringUtils.isBlank(route.getRouteId()))
            throw new CommonException("Service id is empty");
        route.setNewRecord(false);
        route.setCommonValue("", GatewayConstant.SYS_CODE, GatewayConstant.DEFAULT_TENANT_CODE);
        if ((update = this.dao.update(route)) > 0) {
            dynamicRouteService.update(routeDefinition(route));
        }
        return update;
    }

    /**
     * 删除路由
     *
     * @param id id
     * @return Mono
     */
    @Transactional
    public int delete(Long id) {
        Route route = new Route();
        route.setId(id);
        route.setNewRecord(false);
        route.setCommonValue("", GatewayConstant.SYS_CODE, GatewayConstant.DEFAULT_TENANT_CODE);
        int update = this.dao.delete(route);
        dynamicRouteService.delete(id);
        return update;
    }

    /**
     * 刷新路由
     */
    public void refresh() {
        Route init = new Route();
        init.setStatus(CommonConstant.DEL_FLAG_NORMAL.toString());
        List<Route> routes = this.findList(init);
        if (CollectionUtils.isNotEmpty(routes)) {
            log.info("Init {} route records", routes.size());
            for (Route route : routes)
                dynamicRouteService.update(routeDefinition(route));
            // 存入Redis
            redisTemplate.opsForValue().set(CommonConstant.ROUTE_KEY, JsonMapper.getInstance().toJson(routes));
        }
    }

    /**
     * 初始化RouteDefinition
     * @param route
     * @return
     */
    private RouteDefinition routeDefinition(Route route){
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(route.getRouteId());
        if (StringUtils.isNoneBlank(route.getPredicates()))
            routeDefinition.setPredicates(predicateDefinitions(route));
        if (StringUtils.isNoneBlank(route.getFilters()))
            routeDefinition.setFilters(filterDefinitions(route));
        routeDefinition.setUri(URI.create(route.getUri()));
        return routeDefinition;
    }

    /**
     * 初始化predicateDefinition
     * @param route
     * @return
     */
    private List<PredicateDefinition> predicateDefinitions(Route route){
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        try {
            List<RoutePredicateVo> routePredicateVos = JsonMapper.getInstance().fromJson(route.getPredicates(),
                    JsonMapper.getInstance().createCollectionType(ArrayList.class,RoutePredicateVo.class));
            if (CollectionUtils.isNotEmpty(routePredicateVos)){
                for (RoutePredicateVo routePredicateVo : routePredicateVos){
                    PredicateDefinition predicate = new PredicateDefinition();
                    predicate.setName(routePredicateVo.getName());
                    predicate.setArgs(routePredicateVo.getArgs());
                    predicateDefinitions.add(predicate);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return predicateDefinitions;
    }

    /**
     * 初始化FilterDefinition
     * @param route
     * @return
     */
    private List<FilterDefinition> filterDefinitions(Route route){
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        try {
            List<RouteFilterVo> routeFilterVos = JsonMapper.getInstance().fromJson(route.getFilters(),
                    JsonMapper.getInstance().createCollectionType(ArrayList.class,RouteFilterVo.class));
            if (CollectionUtils.isNotEmpty(routeFilterVos)){
                for (RouteFilterVo filterVo : routeFilterVos){
                    FilterDefinition filter = new FilterDefinition();
                    filter.setName(filterVo.getName());
                    filter.setArgs(filterVo.getArgs());
                    filterDefinitions.add(filter);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return filterDefinitions;
    }
}

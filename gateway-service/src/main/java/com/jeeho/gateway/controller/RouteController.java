package com.jeeho.gateway.controller;

import com.github.pagehelper.PageInfo;
import com.jeeho.common.core.constant.CommonConstant;
import com.jeeho.common.core.expcetions.CommonException;
import com.jeeho.common.core.model.ResponseBean;
import com.jeeho.common.core.utils.PageUtil;
import com.jeeho.common.core.web.BaseController;
import com.jeeho.gateway.module.Route;
import com.jeeho.gateway.service.RouteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/route/v1/route")
public class RouteController extends BaseController {
    private final RouteService routeService;

    /**
     * 根据id获取路由信息
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}")
    public Route get(@PathVariable Long id){
        try {
            return routeService.get(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 分页查询获取路由信息
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param route
     * @return
     */
    @GetMapping(path = "routeList")
    public PageInfo<Route> routePageInfo(
            @RequestParam(value = CommonConstant.PAGE_NUM,required = false,defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE,required = false,defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            @RequestParam(value = CommonConstant.SORT,required = false,defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
            @RequestParam(value = CommonConstant.ORDER,required = false,defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
            Route route){
        return routeService.findPage(PageUtil.pageInfo(pageNum,pageSize,sort,order),route);
    }

    @PutMapping
    public ResponseBean<Boolean> updateRoute(@RequestBody @Valid Route route){
        try {
            route.setCommonValue("","","");
            return new ResponseBean<>(routeService.update(route) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    @PostMapping
    public ResponseBean<Boolean> add(@RequestBody @Valid Route route){
        try {
            return new ResponseBean<>(routeService.insert(route) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseBean<Boolean> delete(@PathVariable Long id){
        try {
            return new ResponseBean<>(routeService.delete(id) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    @PostMapping(path = "deleteAll")
    public ResponseBean<Boolean> deleteAll(@RequestBody Long[] ids){
        try {
            return new ResponseBean<>(routeService.deleteAll(ids) > 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    @GetMapping(path = "refresh")
    public ResponseBean<Boolean> refresh(){
        try {
            routeService.refresh();
            return new ResponseBean<>(true);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException(e.getMessage());
        }
    }
}

package com.jeeho.common.core.persistence;

import java.util.List;

import com.jeeho.common.core.constant.CommonConstant;
import org.apache.ibatis.annotations.Param;
/**
 * crud dao
 * @param <T>
 */
public interface CrudMapper<T> extends BaseMapper {
    /**
     * 根据ID查询，统一类型为Long
     *
     * @param id id
     * @return T
     */
    T getById(Long id);

    /**
     * 获取单条数据
     *
     * @param entity entity
     * @return T
     */
    T get(T entity);

    /**
     * 获取列表数据
     *
     * @param entity entity
     * @return List
     */
    List<T> findList(T entity);

    /**
     * 获取全部列表数据
     *
     * @param entity entity
     * @return List
     */
    List<T> findAllList(T entity);

    /**
     * 根据id获取列表数据
     *
     * @param ids ids
     * @return List
     */
    List<T> findListById(@Param(CommonConstant.PARAM_IDS) Long[] ids);

    /**
     * 插入单条数据
     *
     * @param entity entity
     * @return int
     */
    int insert(T entity);

    /**
     * 更新单条数据
     *
     * @param entity entity
     * @return int
     */
    int update(T entity);

    /**
     * 删除单条数据
     *
     * @param entity entity
     * @return int
     */
    int delete(T entity);

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     */
    int deleteAll(@Param(CommonConstant.PARAM_IDS) Long[] ids);
}

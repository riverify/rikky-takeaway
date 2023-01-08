package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.entity.Category;

/**
 * <p>Project: rikky-takeaway - CategoryService 分类Service接口
 * <p>Powered by Riverify On 01-02-2023 21:43:56
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public interface CategoryService extends IService<Category> {

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    void remove(Long id);

}

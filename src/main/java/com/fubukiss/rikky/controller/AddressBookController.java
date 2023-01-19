package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fubukiss.rikky.common.BaseContext;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.AddressBook;
import com.fubukiss.rikky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * <h2>新增地址<h2/>
     *
     * @param addressBook 地址簿，@RequestBody注解表示接收json格式的数据
     * @return {@link R}
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * <h2>设置默认地址<h2/>
     *
     * @param addressBook 地址簿，@RequestBody注解表示接收json格式的数据
     * @return {@link R}
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * <h2>根据id查询地址<h2/>
     *
     * @param id 地址id，@PathVariable注解表示从url中?之前的参数中获取
     * @return {@link R}
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);  // fixme: 回传到前端的数据中，label字段没法对应到前端的label的选中状态
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * <h2>查询默认地址<h2/>
     *
     * @return {@link R}
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * <h2>查询指定用户的全部地址<h2/>
     *
     * @param addressBook 地址簿，@RequestBody注解表示接收json格式的数据
     * @return {@link R}
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }


    /**
     * <h2>修改地址本<h2/>
     *
     * @param addressBook 地址簿，@RequestBody注解表示接收json格式的数据
     * @return {@link R}
     */
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook) {
        log.info("修改后的addressBook:{}", addressBook);
        // SQL: update address_book set name = ?, phone = ?, address = ?, is_default = ?, update_time = ? where id = ?
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }


    /**
     * <h2>根据id删除地址本<h2/>
     *
     * @param ids 地址id
     * @return {@link R}
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除的id:{}", ids);
        addressBookService.removeById(ids);

        return R.success("删除成功");
    }

}

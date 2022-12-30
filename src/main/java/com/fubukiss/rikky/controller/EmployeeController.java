package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.Employee;
import com.fubukiss.rikky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>Project: rikky-takeaway - EmployeeController 员工控制器
 * <p>Powered by Riverify On 12-15-2022 18:51:57
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * <h2>登陆验证</h2>
     * <p>由前端可知，该 login请求为“/employee/login”，同时传入Json格式的 username 和 password。
     *
     * <p>业务逻辑：<br>
     * 1.将页面提交的密码password进行md5加密处理。<br>
     * 2.根据用户名查询数据库。<br>
     * 3.判断查询结果是否为空，如果为空，说明用户名不存在，返回错误信息。<br>
     * 4.如果不为空，说明用户名存在，判断密码是否正确，如果不正确，返回错误信息。<br>
     * 5.查看员工的状态，如果状态为 0，说明该员工已经被禁用，返回错误信息。<br>
     * 6.如果都正确，将员工id存入session中，返回成功信息。
     * </p>
     *
     * @param request  通过获取到的request对象，记录 Session等信息，用于后续的账号验证
     * @param employee 使用 @RequestBody注解以接收这类 json数据格式，Employee内需要有该 json数据中对应的 key的同名成员变量
     * @return 返回通用返回结果类
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1.将页面提交的密码password进行md5加密处理，md5加密并非绝对安全的加密方式，它能够防止密码被明文传输，减小风险。
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // DigestUtils是Spring提供的工具类，用于加密。

        // 2.根据用户名查询数据库。（使用 Mybatis-Plus）
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>(); // LambdaQueryWrapper是Mybatis-Plus提供的一个查询条件构造器
        queryWrapper.eq(Employee::getUsername, employee.getUsername()); // 相当于sql语句中的where部分，设置接下来getOne查询的范围，即数据库的username = 前台获取的username
        Employee emp = employeeService.getOne(queryWrapper); // 相当于数据库查询的select部分，由于数据库已经对username进行了唯一性的约束，故只需要getOne

        // 3.判断查询结果是否为空，如果为空，说明用户名不存在，返回错误信息
        if (emp == null) {
            return R.error("用户名或密码不正确!");
        }

        // 4.如果不为空，说明用户名存在，判断密码是否正确，如果不正确，返回错误信息
        if (!emp.getPassword().equals(password)) {
            return R.error("用户名或密码不正确!");
        }

        // 5.查看员工的状态，如果状态为 0，说明该员工已经被禁用，返回错误信息
        if (emp.getStatus() == 0) {
            return R.error("用户名或密码不正确!");  // 为了安全，这里不返回具体的错误信息
        }

        // 6.如果都正确，将员工id存入session中，返回成功信息
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }


    /**
     * <h2>退出登陆</h2>
     * 只需要清理session中保存的当前登陆员工的id就行了。
     *
     * @param request 将要被销毁的request对象
     * @return 返回通用返回结果类
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理session中保存的当前登陆员工的id
        request.getSession().removeAttribute("employee"); // 放入的时候是什么名字，就要把什么名字移除
        return R.success("退出成功");
    }


    /**
     * <h2>新增员工</h2>
     * <p>新增的员工给予默认密码，密码为身份证后6位。
     *
     * @param request  通过获取当前request，得到操作员的id
     * @param employee 前端传入的员工信息,使用 @RequestBody注解以接收这类 json数据格式，Employee内需要有该 json数据中对应的 key的同名成员变量
     * @return 返回通用返回结果类
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息:{}", employee.toString()); // 使用log4j2打印日志
        // 对员工信息进一步补充
        // 由于前端未传入密码，默认设置密码为身份证后6位，需要md5加密
        String password = employee.getIdNumber().substring(employee.getIdNumber().length() - 6);
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // DigestUtils是Spring提供的工具类，用于加密。
        employee.setPassword(password); // 设置密码
//        employee.setCreateTime(LocalDateTime.now()); // 设置创建时间
//        employee.setUpdateTime(LocalDateTime.now()); // 设置更新时间
//        Long employeeId = (Long) request.getSession().getAttribute("employee");// 获取当前登陆员工的id，用于设置创建人和更新人
//        employee.setCreateUser(employeeId); // 设置创建人id
//        employee.setUpdateUser(employeeId); // 设置更新人id

        // 调用service层的方法，保存员工信息
        employeeService.save(employee); // 由于使用了mybatis-plus，在employeeService中继承了IService接口，所以可以直接调用save方法。

        return R.success("新增员工成功");
    }


    /**
     * <h2>根据name分页查询员工信息</h2>
     * <p>如果name为空，则查询所有员工信息。
     *
     * @param page     前端传入的分页信息，一次性传入当前页码
     * @param pageSize 前端传入的分页信息，一次性传入每页显示的条数
     * @param name     前端传入的查询条件，员工姓名，若为空，则查询所有员工，name和数据库中的字段名一致，mybatis-plus会自动将name转换为数据库中的字段名
     * @return Page为mybatis-plus提供的分页工具类，返回的是一个分页对象，里面包含了分页信息和查询结果
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("分页查询的信息，当前页码:{}，每页显示的条数:{}，查询条件:{}", page, pageSize, name);
        // 构造分页构造器
        Page<Employee> pageParam = new Page<>(page, pageSize); // 传入的参数为当前页码和每页显示的条数，为Mybatis-plus提供的分页工具类
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>(); // 为Mybatis-plus提供的条件构造器
        // 添加过滤条件 like为模糊查询 StringUtils为org.apache.commons.lang.StringUtils，如果name不为空，则添加模糊查询条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name); // 设置查询条件，name为数据库中的字段名，Employee::getName为Employee类中的成员变量名
        // 添加排序条件 按照更新时间降序排序
        queryWrapper.orderByDesc(Employee::getUpdateTime); // 设置排序条件，Employee::getUpdateTime为Employee类中的成员变量名，按照更新时间降序排序
        // 调用service层的方法，查询员工信息
        employeeService.page(pageParam, queryWrapper); // 由于使用了mybatis-plus，在employeeService中继承了IService接口，所以可以直接调用page方法，返回的是一个分页对象，里面包含了分页信息和查询结果
        // 返回结果
        return R.success(pageParam); // 返回分页对象
    }

    /**
     * <h2>根据id修改员工信息</h2>
     *
     * @param employee 前端传入的json数据
     * @return 返回修改结果
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工信息，传入的参数为:{}", employee);

//        long id = Thread.currentThread().getId();                            // 获取当前线程的id
//        log.info("当前线程id={}", id);                                        // Slf4j的日志输出

//        Long operator = (Long) request.getSession().getAttribute("employee");// 获取当前登录用户的id
//        employee.setUpdateTime(LocalDateTime.now());    // 设置更新时间
//        employee.setUpdateUser(operator);               // 设置更新人
        employeeService.updateById(employee); // 由于使用了mybatis-plus，在employeeService中继承了IService接口，所以可以直接调用updateById方法，根据id修改员工信息
        return R.success("员工状态修改成功");
    }


    /**
     * <h2>根据id查询员工信息</h2>
     *
     * @param id 前端传入的员工id，@PathVariable注解表示从路径中获取参数
     * @return 返回查询结果
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息，传入的参数为:{}", id);
        Employee employee = employeeService.getById(id); // 由于使用了mybatis-plus，在employeeService中继承了IService接口，所以可以直接调用getById方法，根据id查询员工信息
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }

}

package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.Employee;
import com.fubukiss.rikky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
     * @param request  通过获取到的request对象，记录 Session等信息，用于后续的账号验证。
     * @param employee 使用 @RequestBody注解以接收这类 json数据格式，Employee内需要有该 json数据中对应的 key的同名成员变量。
     * @return 返回通用返回结果类。
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
     * @param request 将要被销毁的request对象。
     * @return 返回通用返回结果类。
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
     * @param request  通过获取当前request，得到操作员的id。
     * @param employee 前端传入的员工信息,使用 @RequestBody注解以接收这类 json数据格式，Employee内需要有该 json数据中对应的 key的同名成员变量。
     * @return 返回通用返回结果类。
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息:{}", employee.toString()); // 使用log4j2打印日志
        // 对员工信息进一步补充
        // 由于前端未传入密码，默认设置密码为身份证后6位，需要md5加密
        String password = employee.getIdNumber().substring(employee.getIdNumber().length() - 6);
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // DigestUtils是Spring提供的工具类，用于加密。
        employee.setPassword(password); // 设置密码
        employee.setCreateTime(LocalDateTime.now()); // 设置创建时间
        employee.setUpdateTime(LocalDateTime.now()); // 设置更新时间
        Long employeeId = (Long) request.getSession().getAttribute("employee");// 获取当前登陆员工的id，用于设置创建人和更新人
        employee.setCreateUser(employeeId); // 设置创建人id
        employee.setUpdateUser(employeeId); // 设置更新人id

        // 调用service层的方法，保存员工信息
        employeeService.save(employee); // 由于使用了mybatis-plus，在employeeService中继承了IService接口，所以可以直接调用save方法。

        return R.success("新增员工成功");
    }

}

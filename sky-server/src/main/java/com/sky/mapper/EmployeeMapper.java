package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新建员工数据
     * @param employee
     */
    @Insert("INSERT INTO employee (id_number, name, phone, sex, username, create_time, update_time, password, create_user, update_user)" +
            "VALUES (#{idNumber}, #{name}, #{phone}, #{sex}, #{username}, #{createTime}, #{updateTime}, #{password}, #{createUser}, #{updateUser})")
    @AutoFill(OperationType.INSERT)
    void addEmp(Employee employee);   // 你插入数据库的数值不能是空的啊

    Page<Employee> pageSelect(EmployeePageQueryDTO empPage);

    void startOrStop(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 更新员工数据
     * @param employee
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);
}

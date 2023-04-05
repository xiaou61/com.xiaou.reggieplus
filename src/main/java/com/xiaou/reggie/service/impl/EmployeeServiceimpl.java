package com.xiaou.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaou.reggie.entity.Employee;
import com.xiaou.reggie.mapper.EmployeeMapper;
import com.xiaou.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceimpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}

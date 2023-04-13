package com.xiaou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaou.reggie.common.R;
import com.xiaou.reggie.common.SMSUtils;
import com.xiaou.reggie.common.ValidateCodeUtils;
import com.xiaou.reggie.entity.User;
import com.xiaou.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phone=user.getPhone();
        //获取手机号
        if (StringUtils.isNotEmpty(phone)){
            //生成一个随机的四位的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("发送短信验证码：phone={},code={}", phone, code);
            //调用阿里云提供的短信API服务发送短信
//            SMSUtils.sendMessage("Reggie外卖","",phone,code);

            //需要将生成的验证码保存到session中
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }


        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
   @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){

       log.info("map{}",map.toString());

       //获取手机号
       String phone = map.get("phone").toString();
       //获得验证码
       String code = map.get("code").toString();
       //从session中获得保存的验证码
       Object codeInSession = session.getAttribute(phone);
       //进行验证码的比对，页面提交的验证码和session中保存的验证码进行比对
       if (codeInSession != null && codeInSession.equals(code)) {
           //如果能够比对成功，登录成功
               LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
           queryWrapper.eq(User::getPhone,phone);

           User user = userService.getOne(queryWrapper);
           if (user == null) {
               //判断当前手机号对应的用户是否为新用户，如果是新用户，就自动完成注册
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
           }
           //登录成功
           return R.success(user);

       }

       return R.error("发送失败");

   }
    
    
}

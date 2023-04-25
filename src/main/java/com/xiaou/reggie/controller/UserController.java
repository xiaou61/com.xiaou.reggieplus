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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

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
//            session.setAttribute(phone,code);

            //将验证码缓存到redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,1, TimeUnit.MINUTES);

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
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);

        //从redis中获取
        Object codeInSession=redisTemplate.opsForValue().get(phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());


            //如果用户登录成功，删除
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的用户id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
    
}

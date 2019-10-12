package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 张凡 on 2019/9/24.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    /*
     */
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value="login.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value="logout.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value="register.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {

        return iUserService.register(user);
    }

    @RequestMapping(value="check_valid.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkVaild(String str,String type) {
        //
        return iUserService.checkVaild(str,type);
    }

    /*
    获取用户登录的请求信息
     */
    @RequestMapping(value="get_user_info.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    /*
    忘记密码功能
     */
    @RequestMapping(value="forget_get_question.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /*
    校验问题答案是否正确
     */
    @RequestMapping(value="forget_check_answer.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer) {
        return iUserService.checkAnswer(username,question,answer);
    }

    /*
    忘记密码的重置密码
     */
    @RequestMapping(value="forget_reset_password.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken) {
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /*
    在登录状态下修改密码
     */
    @RequestMapping(value="reset_password.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /*
    更新用户信息
     */
    @RequestMapping(value="update_information.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user) {
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == currentUser) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());/*设置用户编号，按照ID去更新*/
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /*
    获取用户的详细信息,如果用户尚未登录需要强制登录
    强制要求密码为空
     */
    @RequestMapping(value="get_information.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session) {
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == currentUser) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，需要强制登录");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}

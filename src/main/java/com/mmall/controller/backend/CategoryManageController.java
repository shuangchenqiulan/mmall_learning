package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mmall.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by 张凡 on 2019/10/11.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "category.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String category, @RequestParam(value = "parentId",defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，请登录");
        }

        //检验一下是否是管理员
        ServerResponse response = iUserService.checkAdminRole(user);
        if(!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
        return null;
    }
}

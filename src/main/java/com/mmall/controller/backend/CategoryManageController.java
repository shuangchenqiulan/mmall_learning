package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
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

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，请登录");
        }

        //检验一下是否是管理员
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()) {
            //增加处理分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "set_category_name.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session,String categoryName,Integer categoryId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，请登录");
        }
        //检验一下是否是管理员
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /*
    获取平级的节点信息
     */

    @RequestMapping(value = "get_category.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParalleCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue = "0")Integer categoryId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，请登录");
        }
        //检验一下是否是管理员
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()) {
            /*
            查询子节点的信息，不递归保持平级
             */
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /*
    递归查询子节点的信息
     */
    @RequestMapping(value = "get_deep_category.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LONGIN.getCode(),"用户未登录，请登录");
        }
        //检验一下是否是管理员
        ServerResponse response = iUserService.checkAdminRole(user);
        if(response.isSuccess()) {
            /*
            查询子节点的信息，不递归保持平级
             */
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}

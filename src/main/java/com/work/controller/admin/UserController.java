package com.work.controller.admin;

import com.work.been.AjaxResult;
import com.work.been.PageBean;
import com.work.model.User;
import com.work.service.UserService;
import com.work.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping("admin/user")
public class UserController extends BaseAdminController<User, String>{
    @Autowired
    private UserService userService ;

    @RequestMapping(value = "add" ,method = RequestMethod.POST)
    public String add(User user , RedirectAttributes redirectAttributes){
        try {
            user.setPassword(Encrypt.e(user.getPassword()));       // md5加密
            user.setDate(new Date());
            this.userService.add(user) ;
          //  redirectAttributes.addAttribute("msg",RESULT_OK);
            redirectAttributes.addFlashAttribute("result", new AjaxResult(true, "注册成功"));
            return "loginUI";
        }catch(Exception e){
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("msg", RESULT_ERROR);
        return "redirect:/registerUI";
    }

    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    public String login(User user , HttpSession session, RedirectAttributes redirectAttributes){
        try {
            User loginUser=this.userService.login(user);
            if (loginUser!=null){
                session.setAttribute("loginUser",loginUser);
                redirectAttributes.addFlashAttribute("result", new AjaxResult(true, "登录成功"));
                return "index";
            }else {
                redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "用户名或者密码错误"));
                return "redirect:/loginUI";
            }
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "发生错误"));
            return "redirect:/loginUI";
        }
    }

    /**
     * 异步删除
     * @param u_id
     * @return
     */
    @RequestMapping(value = "/delete/{u_id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable int u_id) {
        try {
            this.userService.deleteById(u_id);
            return successResult;
        }catch(Exception e){
            e.printStackTrace();
        }
        return errorResult;
    }
    @RequestMapping("list")
    public String list(){
        return TEMPLATE_PATH+"list";
    }
    /**
     * 异步获取dataTable
     * @param searchText
     * @param sEcho
     * @param pageBean
     * @return
     */
    @RequestMapping("dataTable")
    @ResponseBody
    public Map dataTable(String searchText, int sEcho, PageBean pageBean) {
        return userService.dataTable(searchText, sEcho, pageBean);
    }

    @RequestMapping(value = "update" ,method = RequestMethod.POST)
    public String update(User user , RedirectAttributes redirectAttributes){
        this.userService.updateInfo(user) ;
        redirectAttributes.addFlashAttribute("result", successResult);
        return REDIRECT_URL+"list";
    }

    /**
     * 新增页
     * @return
     */
    @RequestMapping(value = "/saveUI", method = RequestMethod.GET)
    public String saveUI() {
        return TEMPLATE_PATH + "saveUI";
    }

    @RequestMapping("saveUI/{u_id}")
    public String saveUI(@PathVariable int u_id, Model model){
        model.addAttribute(this.userService.selectById(u_id));      //未知原因,查不出id...
        model.addAttribute("u_id",u_id);
        return TEMPLATE_PATH + "saveUI";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("loginUser");
        return "index";
    }

}

package com.work.controller.admin;

import com.work.been.AjaxResult;
import com.work.been.PageBean;
import com.work.model.Goods;
import com.work.service.GoodsService;
import com.work.service.Goods_typeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Map;

/**
 * Created by 林智 on 2016/8/7.
 */
@Controller
@RequestMapping(value = "admin/goods")
public class GoodsController extends BaseAdminController<Goods,Long>{
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private Goods_typeService goods_typeService;

    @RequestMapping("list")
    public String list(){
        return TEMPLATE_PATH + "list";
    }

    @RequestMapping("dataTable")
    @ResponseBody
    public Map dataTable(String searchText, int sEcho, PageBean pageBean){
        return goodsService.dataTable(searchText,sEcho,pageBean);
    }

    @RequestMapping("update")
    public String update(@RequestParam("picture") MultipartFile picture, String name,
                         double money, int goods_type_id, String introduction, int g_id,
                         RedirectAttributes redirectAttributes){
        try {
            Goods goods = goodsService.selectById(g_id);
            goods.setMoney(money);
            goods.setIntroduction(introduction);
            goods.setName(name);
            goods.setGoods_type_id(goods_type_id);
            if(!goodsService.saveOrUpdatePicture(goods, picture)){
                redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "操作失败,图片的格式只能为.jpg或.png或.bmp"));
                return REDIRECT_URL + "list";
            }
            goodsService.updateInfo(goods);
            redirectAttributes.addFlashAttribute("result", new AjaxResult(true, "操作成功"));
            return REDIRECT_URL+"list";
        }catch (Exception e){
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "操作失败"));
        return REDIRECT_URL+"list";
    }

    @RequestMapping(value = "/saveUI", method = RequestMethod.GET)
    public String saveUI(Model model){
        model.addAttribute("goods_type_list", goods_typeService.selectAll());
        return TEMPLATE_PATH+"saveUI";
    }

    @RequestMapping("saveUI/{gt_id}")
    public String saveUI(@PathVariable int gt_id, Model model){
        model.addAttribute("goods",goodsService.selectById(gt_id));
        model.addAttribute("goods_type_list", goods_typeService.selectAll());
        return TEMPLATE_PATH+"saveUI";
    }


    @RequestMapping("delete/{gt_id}")
    @ResponseBody
    public AjaxResult delete(@PathVariable int gt_id){
        try {
            //   majorService.deleteByDepartId(gt_id);
            goodsService.deleteById(gt_id);
            return new AjaxResult(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new AjaxResult(false,"操作失败");
        }
    }

    @RequestMapping("show/{gt_id}")
    public String show(@PathVariable int gt_id,Model model){
        model.addAttribute("goods",goodsService.selectById(gt_id));
        return TEMPLATE_PATH+"show";
    }

    @RequestMapping("add")
    public String add(@RequestParam("picture") MultipartFile picture, String name,
                      double money, int goods_type_id, String introduction,
                      RedirectAttributes redirectAttributes){       //参数没办法带Goods对象,报400
        try {
            Goods goods = new Goods();
            goods.setDate(new Date());
            goods.setName(name);
            goods.setGoods_type_id(goods_type_id);
            goods.setIntroduction(introduction);
            goods.setMoney(money);
            System.out.println(picture + "---------------");
            if(!goodsService.saveOrUpdatePicture(goods, picture)){
                redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "操作失败,图片的格式只能为.jpg或.png或.bmp"));
                return REDIRECT_URL + "list";
            }
            goodsService.addGoods(goods);
            redirectAttributes.addFlashAttribute("result", new AjaxResult(true, "操作成功"));
            return REDIRECT_URL + "list";
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("result", new AjaxResult(false, "操作失败,内部错误"));
        }
        return REDIRECT_URL+"list";
    }
}

package com.work.controller.user;


import com.work.been.AjaxResult;
import com.work.util.JsonMapper;
import com.work.util.Reflections;

import java.io.Serializable;


public abstract class BaseUserController<T, ID extends Serializable> {
    protected Class<T> entityClass = Reflections.getClassGenricType(getClass());//获取泛型
    protected String claseName=entityClass.getName().substring(entityClass.getName().lastIndexOf(".")+1);
    protected String TEMPLATE_NAME=claseName.substring(0,1).toLowerCase()+claseName.substring(1);  //类名

    protected String user_dir = "/WEB-INF/user/";                   //后台管理的jsp目录
    protected String TEMPLATE_PATH=user_dir+TEMPLATE_NAME+"/";              //每个模块的页面地址
    protected String REDIRECT_URL="redirect:/user/"+TEMPLATE_NAME+"/";      //重定向    //重定向


    protected static JsonMapper mapper = JsonMapper.nonNullMapper();  //json工具
    protected static AjaxResult ajxlist = new AjaxResult();    //json封装
    protected final static AjaxResult errorResult = new AjaxResult(false,
            "操作失败！");

    protected final static AjaxResult successResult = new AjaxResult(true,
			"操作成功！");
    protected final static String RESULT_OK;
    protected final static String RESULT_ERROR;
    static {
        RESULT_OK = mapper.toJson(successResult);  //java对象转换成json对象
        RESULT_ERROR = mapper.toJson(errorResult);
    }

}

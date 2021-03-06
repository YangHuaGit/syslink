package com.tongyuan.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.tongyuan.model.domain.Operationlog;
import com.tongyuan.model.service.OperationlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by yh on 2017/9/1.
 */

@Controller
@RequestMapping("/api/operationlog")
public class OperationlogController extends BaseController{

    @Autowired
    private OperationlogService operationlogService;

    @RequestMapping(value = "/list",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject finAllLog()
    {
        JSONObject jo = new JSONObject();
        List<Operationlog> operationlogList = new ArrayList<>();
        try
        {
            List <Operationlog>operationlogs = operationlogService.findAllLog();
            operationlogList.addAll(operationlogs);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取列表失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取列表成功");
        jo.put("logs",operationlogList);
        return (JSONObject) JSONObject.toJSON(jo);
    }


    @RequestMapping(value = "/query",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject queryLog(@RequestBody String para)
    {
        JSONObject jo = new JSONObject();

//        List<Operationlog> operationlogList = new ArrayList<>();
        Page<Operationlog>page = new Page<>();
        JSONObject jsonObject = JSON.parseObject(para);
        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("realName",jsonObject.getString("realName"));
        map.put("content",jsonObject.getString("content"));
        map.put("pageIndex",jsonObject.getIntValue("pageIndex"));
        map.put("pageSize",jsonObject.getIntValue("pageSize"));
        if(jsonObject.getJSONArray("time")!=null)
        {
            map.put("startTime",jsonObject.getJSONArray("time").getString(0));
            map.put("endTime",jsonObject.getJSONArray("time").getString(1));
        }




        try
        {
//            List <Operationlog>operationlogs = operationlogService.query(map);

//            operationlogList.addAll(operationlogs);
            page = operationlogService.find(map);


            Map<String,Object> params = new HashMap<String,Object>();



        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取列表失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取列表成功");

//        PageInfo<Operationlog> pageInfo =new PageInfo<Operationlog>(operationlogList);
        jo.put("logs",page);
        jo.put("total",page.getTotal());

        return (JSONObject) JSONObject.toJSON(jo);
    }





}

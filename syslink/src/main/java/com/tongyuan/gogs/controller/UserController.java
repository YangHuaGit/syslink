package com.tongyuan.gogs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tongyuan.gogs.domain.GUser;
import com.tongyuan.gogs.service.GUserService;
import com.tongyuan.model.controller.BaseController;
import com.tongyuan.model.domainmodel.LoginedUserModel;
import com.tongyuan.model.service.OperationlogService;
import com.tongyuan.model.service.RoleService;
import com.tongyuan.model.wrapper.GUserWarpper;
import com.tongyuan.util.Convert;
import com.tongyuan.util.DateUtil;
import com.tongyuan.util.EncodePasswd;
import com.tongyuan.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;


/**
 * Created by yh on 2017/8/31.
 */
@Controller
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private GUserService userService;
    @Autowired
    private OperationlogService operationlogService;
    @Autowired
    private RoleService roleService;





    @RequestMapping(value = "/assignRole",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject assignRole(@RequestBody Map<String,Object> map, HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        //  Map<String,Object>roles = new HashMap<>();
        //   roles.put("roles",map.get("assigned").toString());

//        roles.put("uid",Integer.parseInt(map.get("uid").toString()));
        String a = map.get("assigned").toString();
        a=a.substring(1,a.length()-1);

        Integer []roles =   Convert.toIntArray(",",a);
        //      map.put("roles",map.get("permissionId").toString());
//        permissionItem.setCreateDate(DateUtil.getCurrentTime());

        try
        {
            roleService.updateUserRoles(Integer.parseInt(map.get("uid").toString()),roles);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","分配角色失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","分配角色成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }

    @RequestMapping(value = "/queryUserRoles",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject queryUserRoles(@RequestBody Map<String,Object> map, HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        List<Integer> userRoles = new ArrayList<>();
        long uid = Long.parseLong(map.get("uid").toString());
        try
        {

            userRoles = roleService.queryUserRoleByUid(uid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取用户角色失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取用户角色成功");
        //   jo.put("userRoles",new UserRoleWarpper(list).warp());
        jo.put("userRoles",userRoles);
        return (JSONObject) JSONObject.toJSON(jo);
    }


    @RequestMapping(value = "/query",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject query(@RequestBody Map<String,Object> map, HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        List<Map<String,Object>> list = new ArrayList<>();
        try
        {
               list = userService.queryUser(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取用户列表失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取用户列表成功");
        jo.put("users",new GUserWarpper(list).warp());
        return (JSONObject) JSONObject.toJSON(jo);
    }





    @RequestMapping(value = "/addGuser",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject addGUser(@RequestBody Map<String,Object> map,HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        JSONObject jo = new JSONObject();

        if(userService.nameExist(map))
        {
            jo.put("flag",false);
            jo.put("msg","该用户名已存在");
            return jo;
        }
        else if (userService.emailExist(map))
        {
            jo.put("flag",false);
            jo.put("msg","该邮箱已存在");
            return jo;
        }


        String a = UUID.randomUUID().toString().replaceAll("-","");
        String b = UUID.randomUUID().toString().replaceAll("-","");
        map.put("rands",a.substring(0,10));
        map.put("salt",b.substring(0,10));
        String passwd = map.get("passwd").toString();
        String newPasswd = EncodePasswd.getEncryptedPassword(passwd,b.substring(0,10),10000,50);
        map.put("passwd",newPasswd);


        try
        {

            userService.addGUser(map);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","添加用户失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","添加用户成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }


    @RequestMapping(value = "/updateGUser",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject updateGUser(@RequestBody Map<String,Object> map,HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {


        JSONObject jo = new JSONObject();
        map.put("updatedUnix",System.currentTimeMillis()/1000L);
        if(map.get("passwd")!=null)
        {
            if(map.get("passwd").toString().equalsIgnoreCase(""))
            {
                map.remove("passwd");
            }
            else
            {
                String a = UUID.randomUUID().toString().replaceAll("-","");
                String b = UUID.randomUUID().toString().replaceAll("-","");
                map.put("rands",a.substring(0,10));
                map.put("salt",b.substring(0,10));
                String passwd = map.get("passwd").toString();
                String newPasswd = EncodePasswd.getEncryptedPassword(passwd,b.substring(0,10),10000,50);
                map.put("passwd",newPasswd);

            }
        }


        try
        {

            userService.updateUser(map);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","编辑用户失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","编辑用户成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }



    @RequestMapping(value = "/deleteGUser",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject deleteGUser(HttpServletRequest request,@RequestBody String para)
    {
        JSONObject jo = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(para);
        long id = jsonObject.getIntValue("id");

        try
        {
            userService.deleteUser(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","删除用户失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","删除用户成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }




    @RequestMapping(value = "/deleteGUsers",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject deleteGUsers(HttpServletRequest request,@RequestBody String para)
    {
        JSONObject jo = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(para);
        JSONArray ids = jsonObject.getJSONArray("ids");

        try
        {
            for(int i=0;i<ids.size();i++)
            {
                userService.deleteUser(ids.getIntValue(i));
            }
            //   userService.delete(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","删除用户失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","删除用户成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }



    @RequestMapping(value = "/getUserId",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserId(HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();


        long uerId  = getCurrentUserId(request);
        jo.put("userId",uerId);
        return (JSONObject) JSONObject.toJSON(jo);
    }
    @RequestMapping(value = "/querySimpleUser",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject querySimpleUser(@RequestBody String para, HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(para);
        List<Map<String,Object>> list = new ArrayList<>();
        try
        {
            list = userService.queyrSimpleUser(jsonObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取用户列表失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取用户列表成功");
        jo.put("simpleUser",list);
        return (JSONObject) JSONObject.toJSON(jo);
    }


    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserInfo( HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        LoginedUserModel loginedUserModel = new LoginedUserModel();
        GUser user = userService.queryById(getCurrentUserId(request));

        try
        {
            loginedUserModel = userService.CreateLoginedUser(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取用户信息失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取用户信息成功");
        jo.put("userInfo",loginedUserModel);
        return (JSONObject) JSONObject.toJSON(jo);
    }

    @RequestMapping(value = "/getUserInfoFirst",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserInfoFirst(@RequestBody String uid, HttpServletRequest request,HttpServletResponse response)
    {
        JSONObject jo = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(uid);
        Long id = jsonObject.getLongValue("uid");
        LoginedUserModel loginedUserModel = new LoginedUserModel();
        GUser user = userService.queryById(id);
        Cookie[] cookies = request.getCookies();
        for (int i =0;i<cookies.length;i++)
        {
            System.out.println(cookies[i].getName());
            System.out.println(cookies[i].getValue());
        }


        Cookie c = new Cookie("gogs_awesome",user.getName());
        c.setDomain(".modelica-china.com");
        c.setMaxAge(60);
        c.setPath("/");
        response.addCookie(c);


        HttpSession session = request.getSession();

        //  session.setAttribute("user", user);
        session.setAttribute("uid",user.getID());
        session.setAttribute("user", user);
        session.setAttribute("base_path", request.getContextPath());
        String lginIp = IpUtil.getIpAddr(request);
        Date loginDate = DateUtil.getTimestamp();
        userService.updateLoginstate(user.getID(),lginIp,loginDate);


        operationlogService.addLog("登录","登录系统",request);

        try
        {
            loginedUserModel = userService.CreateLoginedUser(user);
            setSessionUser(request,loginedUserModel);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","获取用户信息失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","获取用户信息成功");
        jo.put("userInfo",loginedUserModel);
        return (JSONObject) JSONObject.toJSON(jo);
    }



    @RequestMapping(value = "/assign",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject assign(@RequestBody String para, HttpServletRequest request)
    {
        JSONObject jo = new JSONObject();
        // JSONObject jsonObject = JSON.parseObject(para);
        JSONObject jsonObject = JSONObject.parseObject(para);

        //  JSONArray jsonArray = JSONArray.parseArray(para);
        JSONArray jsonArray = jsonObject.getJSONArray("authIds");
        Integer []authIds = new Integer[jsonArray.size()];

        for(int i=0;i<jsonArray.size();i++)
        {
            authIds[i]=jsonArray.getJSONObject(i).getIntValue("authId");
        }

        //   map.put("permissions",map.get("permissionId").toString());
//        permissionItem.setCreateDate(DateUtil.getCurrentTime());

        //      String a = map.get("permissionId").toString();
        //     a=a.substring(1,a.length()-1);
        Integer uid = jsonObject.getIntValue("uid");
        //     Integer []authIds =   Convert.toIntArray(",",a);

        try
        {
            userService.updateAuth(uid,authIds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jo.put("flag",false);
            jo.put("msg","分配权限失败");
            return jo;
        }
        jo.put("flag",true);
        jo.put("msg","分配权限成功");
        return (JSONObject) JSONObject.toJSON(jo);
    }



    @RequestMapping(value = "/destory",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject destory(HttpServletRequest request, HttpServletResponse response)
    {

        JSONObject jo = new JSONObject();
        HttpSession session = request.getSession();
        System.out.println(session.getId());

        Cookie[] cookies = request.getCookies();
        for (int i =0;i<cookies.length;i++)
        {
            System.out.println(cookies[i].getName());
            System.out.println(cookies[i].getValue());
            if (cookies[i].getName().equalsIgnoreCase("gogs_awesome"))
            {
                cookies[i].setMaxAge(0);
            }
        }
        return (JSONObject) JSONObject.toJSON(jo);
    }
}

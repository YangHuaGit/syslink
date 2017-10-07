package com.tongyuan.gogs.service.impl;

import com.tongyuan.gogs.dao.GUserMapper;
import com.tongyuan.gogs.dao.OrgUserMapper;
import com.tongyuan.gogs.dao.TeamMapper;
import com.tongyuan.gogs.dao.TeamUserMapper;
import com.tongyuan.gogs.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by yh on 2017/9/25.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private GUserMapper gUserMapper;
    @Autowired
    private OrgUserMapper orgUserMapper;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamUserMapper teamUserMapper;


    @Override
    public List<Map<String,Object>> query(Map<String,Object>map)
    {
        return this.gUserMapper.queryOrg(map);
    }
    @Override
    public boolean addOrg(Map<String,Object>map){

        map.put("lowerName",map.get("name").toString());
        map.put("fullName","");
        map.put("email","");
        map.put("passwd","");
        String a = UUID.randomUUID().toString().replaceAll("-","");
        String b = UUID.randomUUID().toString().replaceAll("-","");
        map.put("rands",a.substring(0,10));
        map.put("salt",b.substring(0,10));
        map.put("loginType",0);
        map.put("loginSource",0);
        map.put("loginName","");
        map.put("type",1);
        map.put("location","");
        map.put("website","");
        map.put("lastRepoVisibility",0);
        map.put("createdUnix", System.currentTimeMillis()/1000L);
        map.put("updatedUnix",System.currentTimeMillis()/1000L);
        map.put("maxRepoCreation",-1);
        map.put("isActive",1);
        map.put("isAdmin",0);
        map.put("allowGitHook",0);
        map.put("allowImportLocal",0);
        map.put("prohibitLogin",0);
        map.put("useCustomAvatar",1);
        map.put("numFollowers",0);
        map.put("numFollowing",0);
        map.put("numStars",0);
        map.put("numRepos",0);
        map.put("description","");
        map.put("numTeams",0);
        map.put("numMembers",0);
        map.put("avatar","");
        map.put("avatarEmail","");
        boolean success = gUserMapper.add(map);

        Map<String,Object> orgUser = new HashMap<>();
        orgUser.put("uid",Long.parseLong(map.get("uid").toString()));
        orgUser.put("orgId",Long.parseLong(map.get("id").toString()));
        orgUser.put("isPublic",0);
        orgUser.put("isOwner",0);
        orgUser.put("numTeams",1);
        success=success&orgUserMapper.add(orgUser);


        Map<String,Object> team = new HashMap<>();
        team.put("orgId",Long.parseLong(map.get("id").toString()));
        team.put("lowerName","owners");
        team.put("name","Owners");
        team.put("description","");
        team.put("authorize",4);
        team.put("numRepos",0);
        team.put("numMembers",0);
        success=success&teamMapper.add(team);


        Map<String,Object> teamUser = new HashMap<>();
        teamUser.put("orgId",Long.parseLong(map.get("id").toString()));
        team.put("teamId",Long.parseLong(team.get("id").toString()));
        team.put("uid",Long.parseLong(map.get("uid").toString()));
        success=success&teamUserMapper.add(teamUser);




        return  success;
    }

    @Override
    public boolean updateOrg(Map<String,Object>map)
    {
        return this.gUserMapper.updateOrg(map);
    }

    @Override
    public boolean deleteOrg(long id)
    {
        return  teamMapper.delete(id)&orgUserMapper.delete(id)&teamUserMapper.delete(id)&gUserMapper.delete(id);
    }


    @Override
    public boolean nameExist(Map<String,Object>map)
    {
        Map<String,Object> mapName = new HashMap<>();

        mapName.put("name",map.get("name").toString());

        List<Map<String,Object>> name  =  gUserMapper.queryOrg(map);

        return name.size()>0;

    }


    @Override
    public List<Map<String,Object>> getOrgUser(Map<String,Object>map)
    {

       List<Map<String,Object>> orgUser = orgUserMapper.queryByOrgId(getOrgIdByName(map));

       for(Map<String,Object>objectMap:orgUser)
       {
            Map<String,Object> simpleUser = gUserMapper.queryUserById(Long.parseLong(objectMap.get("uid").toString()));
            objectMap.put("name",simpleUser.get("name"));
            objectMap.put("fullName",simpleUser.get("fullName"));
       }
       return orgUser;

    }


    @Override
    public Long getOrgIdByName(Map<String,Object>map)
    {
        List<Map<String,Object>>list  =  gUserMapper.queryOrg(map);
        if(list.size()>0)
        {
            return Long.parseLong(list.iterator().next().get("id").toString()) ;
        }
        else
        {
            return null;
        }
    }





    @Override
    public List<Map<String,Object>> getMyOrg(long uid)
    {
        List<Map<String,Object>>orgUser = orgUserMapper.queryByUid(uid);
        List<Map<String,Object>>list1 = new ArrayList<>();
        for(Map<String,Object>map1:orgUser)
        {
            if(
                   // (map1.get("isOwner").toString().equalsIgnoreCase("true"))&&
                    (Long.parseLong(map1.get("uid").toString())==uid))
            {
                list1.add(map1);
            }
        }
        List<Map<String,Object>>myOrg = new ArrayList<>();
        for(Map<String ,Object>map2:list1)
        {
             Map<String ,Object> org =  gUserMapper.queryOrgById(Long.parseLong(map2.get("orgId").toString()));
             myOrg.add(org);
        }
        return myOrg;
    }


    @Override
    public boolean addOrgUser (Map<String,Object>map)
    {
        Map<String,Object>map1 = new HashMap<>();
        map1.put("name",map.get("orgName").toString());
        long orgId = getOrgIdByName(map1);
        long uid  = Long.parseLong(gUserMapper.queryUserByName(map.get("name").toString()).get("id").toString());
        Map<String ,Object>org = gUserMapper.queryOrgById(orgId);
        Map<String,Object>orgUser = new HashMap<>();
        orgUser.put("orgId",orgId);
        orgUser.put("uid",uid);
        orgUser.put("isPublic",0);
        orgUser.put("isOwner",0);
        org.put("numTeams",Long.parseLong(org.get("numTeams").toString()));

        org.put("numMembers",Long.parseLong(org.get("numMembers").toString())+1);
        gUserMapper.updateOrg(org);

        return this. orgUserMapper.add(orgUser);


    }

    @Override
    public boolean updateOrgUser(Map<String,Object>map)
    {
        return this.orgUserMapper.updateByUid(map);
    }

    @Override
    public boolean deleteOrgUser(Map<String ,Object>map)
    {
        Map<String ,Object> org = gUserMapper.queryOrgById(Long.parseLong(map.get("orgId").toString()));
        if(Long.parseLong(org.get("numMembers").toString())>0)
        {
            map.put("numMembers",Long.parseLong(org.get("numMembers").toString())-1);
        }
        gUserMapper.updateOrg(map);
        return this.orgUserMapper.deleteOrgUser(map);
    }

}

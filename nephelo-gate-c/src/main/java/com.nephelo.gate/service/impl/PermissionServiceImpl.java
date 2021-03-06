package com.nephelo.gate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nephelo.api.vo.authority.PermissionInfo;
import com.nephelo.gate.feign.IUserServiceFeign;
import com.nephelo.gate.service.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nephelo on 2018/12/21.
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    private Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    private IUserServiceFeign iUserService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        logger.info("nephelo:hasPermission");
        Object principal = authentication.getPrincipal();
        List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        logger.info("nephelo:grantedAuthorityList:{}", JSONObject.toJSON(grantedAuthorityList));
        boolean hasPermission = false;

        if (principal != null) {
            Set<String> roleSet = new HashSet<>();
            grantedAuthorityList.forEach(grantedAuthority -> roleSet.add(grantedAuthority.getAuthority()));
            String roles = StringUtils.join(roleSet, ";");
            Set<PermissionInfo> permissionInfos = iUserService.findPermissionInfoByRoles(roles);
            logger.info("nephelo:PersissionInfos：{}", JSONObject.toJSON(permissionInfos));
            logger.info("nephelo:request.getRequestURI()：{}", request.getRequestURI());
            for (PermissionInfo permissionInfo : permissionInfos) {
                if (antPathMatcher.match(permissionInfo.getUri(), request.getRequestURI())
                        && request.getMethod().equalsIgnoreCase(permissionInfo.getMethod())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        logger.info("FCat:hasPermission:{}", hasPermission);
        return hasPermission;
    }
}

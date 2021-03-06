package com.nephelo.user.mapper;

import com.nephelo.user.bean.TUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TUserMapper extends Mapper<TUser> {
    TUser getByUsername(String userName);

    List<TUser> getByKey(String key);

    List<TUser> getLeadersByGroupId(Integer groupId);

    List<TUser> getMembersByGroupId(Integer groupId);
}

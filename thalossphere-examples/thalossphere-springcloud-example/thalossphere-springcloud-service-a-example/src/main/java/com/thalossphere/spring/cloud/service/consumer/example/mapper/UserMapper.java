package com.thalossphere.spring.cloud.service.consumer.example.mapper;

import com.thalossphere.spring.cloud.service.consumer.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from t_user where id = #{id}")
    User findById(int id);

}

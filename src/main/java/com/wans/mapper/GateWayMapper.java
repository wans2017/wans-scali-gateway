package com.wans.mapper;

import com.wans.entity.GateWay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by admin on 2020/9/22.
 */
@Mapper
public interface GateWayMapper {
    @Select("SELECT ID AS ID, route_id as routeId, route_name as routeName,route_pattern as routePattern " +
            ",route_type as routeType,route_url as routeUrl " +
            " FROM gateway ")
    public List<GateWay> gateWayAll();

    @Update("update gateway set route_url=#{routeUrl} where route_id=#{routeId};")
    public Integer updateGateWay(@Param("routeId") String routeId, @Param("routeUrl") String routeUrl);

}

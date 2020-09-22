package com.wans.controller;

import com.wans.service.GateWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2020/9/22.
 */
@RestController
public class GateWayController {
    @Autowired
    private GateWayService gateWayService;
    /*
    同步网关配置项
     */
    @GetMapping("/syncGateWayCfg")
    public String syncGateWayCfg() {
        return gateWayService.loadRoute();
    }
}

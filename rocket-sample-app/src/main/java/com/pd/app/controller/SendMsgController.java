package com.pd.app.controller;

import com.pd.app.service.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author peramdy on 2018/9/29.
 */
@RestController
@RequestMapping("/send")
public class SendMsgController {

    @Autowired
    private SendMsgService sendMsgService;

    @RequestMapping(value = "/msg", method = RequestMethod.POST)
    public String msg() {
        sendMsgService.sendMsg();
        return "hello";
    }

}

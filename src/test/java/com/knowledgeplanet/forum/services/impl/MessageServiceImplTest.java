//package com.knowledgeplanet.forum.services.impl;
//
//import com.knowledgeplanet.forum.model.Message;
//import com.knowledgeplanet.forum.services.IMessageService;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest
//class MessageServiceImplTest {
//
//    @Resource
//    private IMessageService messageService;
//
//    @Test
//    void selectByReceiveUserId() {
//        List<Message> messages = messageService.selectByReceiveUserId(12l);
//        System.out.println(messages);
//    }
//
//    @Test
//    void reply() {
//        // 构造对象
//        Message message = new Message();
//        message.setPostUserId(12L);
//        message.setReceiveUserId(21L);
//        message.setContent("回复一下");
//        messageService.reply(10L, message);
//    }
//}
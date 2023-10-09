package com.wang.scaffold.user;

import com.wang.scaffold.user.eventpub.RoleModifyEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName PublishTest.java
 * @Description TODO
 * @createTime 2023年06月26日 14:49:00
 */
@SpringBootTest
public class PublishTest {

    @Resource
    RoleModifyEventPublisher roleModifyEventPublisher;

    @Test
    public void publishTest(){
        roleModifyEventPublisher.publish();
    }
}

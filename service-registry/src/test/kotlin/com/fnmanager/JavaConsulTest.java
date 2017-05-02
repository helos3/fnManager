package com.fnmanager;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import org.junit.Test;

import java.util.UUID;

public class JavaConsulTest {

    @Test
    public void test() {
        String id = UUID.randomUUID().toString();
        Consul cl = Consul.builder().build();
        cl.agentClient().register(
            ImmutableRegistration.builder()
                .port(80)
                .address("87.240.165.82")
                .name("somename")
                .id(id)
                .check(Registration.RegCheck.script("/usr/bin/telnet ${address!!} ${port!!}", 3))
                .build()
        );
        try {
            cl.agentClient().pass(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

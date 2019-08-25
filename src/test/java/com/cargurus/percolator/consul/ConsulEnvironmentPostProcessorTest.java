package com.cargurus.percolator.consul;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

@RunWith(MockitoJUnitRunner.class)
public class ConsulEnvironmentPostProcessorTest {

    @Mock private ConsulHelper consulHelper;

    @Test
    public void testDiscoveryAsksConsul() {
        Environment env = new MockEnvironment().
            withProperty("percolator.consul.base-url", "consul-base-url").
            withProperty("percolator.consul.discovery[0].cluster-name", "cluster name 0").
            withProperty("percolator.consul.discovery[0].service-name", "service name 0").
            withProperty("percolator.consul.discovery[1].cluster-name", "cluster name 1").
            withProperty("percolator.consul.discovery[1].service-name", "service name 1");

        Mockito.
            when(consulHelper.getServiceNodes("service name 0", null)).
            thenReturn(Collections.singletonList(new ServiceNode()));
        Mockito.
            when(consulHelper.getServiceNodes("service name 1", null)).
            thenReturn(Collections.singletonList(new ServiceNode()));

        new ConsulEnvironmentPostProcessor().getConsulClusterProperties(env, consulHelper);

        Mockito.verify(consulHelper).getServiceNodes("service name 0", null);
        Mockito.verify(consulHelper).getServiceNodes("service name 1", null);
        Mockito.verifyNoMoreInteractions(consulHelper);
    }

    @Test(expected = RuntimeException.class)
    public void testErrorNoNodes() {
        Environment env = new MockEnvironment().
            withProperty("percolator.consul.base-url", "consul-base-url").
            withProperty("percolator.consul.discovery[0].cluster-name", "cluster name 0").
            withProperty("percolator.consul.discovery[0].service-name", "service name 0");

        Mockito.
            when(consulHelper.getServiceNodes("service name 0", null)).
            thenReturn(Collections.emptyList());

        new ConsulEnvironmentPostProcessor().getConsulClusterProperties(env, consulHelper);
    }
}

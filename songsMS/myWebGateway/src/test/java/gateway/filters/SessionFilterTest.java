package gateway.filters;


import gateway.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


// stack
@SpringBootTest()
class SessionFilterTest {

    @Autowired
    private Validation validation;
    private SessionFilter sessionFilter;
    private GatewayFilterChain gatewayFilterChain;

    @BeforeEach
    void setUp() {
        validation = new Validation();
        sessionFilter = new SessionFilter(validation);
        gatewayFilterChain = mock(GatewayFilterChain.class);
        when(gatewayFilterChain.filter(ArgumentMatchers.any())).thenReturn(Mono.empty());
    }

    @Test
    void apply_adds_session() {
        MockServerHttpResponse response = new MockServerHttpResponse();
        response.getHeaders().add("TOKEN", "token");
        response.getHeaders().add("USERID", "mmuster");
        response.setStatusCode(HttpStatus.OK);
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        when(exchange.getResponse()).thenReturn(response);
        GatewayFilter filter = sessionFilter.apply(new SessionFilter.Config());
        filter.filter(exchange, gatewayFilterChain).block();  // for reactive methods
        assertTrue(validation.validSession("token"));
    }
}
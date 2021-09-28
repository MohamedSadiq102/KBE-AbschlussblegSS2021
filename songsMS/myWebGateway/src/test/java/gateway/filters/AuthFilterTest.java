package gateway.filters;

import gateway.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthFilterTest {

    @Autowired
    private Validation validation;
    private GatewayFilterChain gatewayFilterChain;
    private ArgumentCaptor<ServerWebExchange> captor;
    private SongFilter authFilter;

    @BeforeEach
    void setUp() {
        validation = new Validation();
        authFilter = new SongFilter(validation);
        gatewayFilterChain = mock(GatewayFilterChain.class);
        captor = ArgumentCaptor.forClass(ServerWebExchange.class);
        when(gatewayFilterChain.filter(captor.capture())).thenReturn(Mono.empty());
    }

    @Test
    void apply_adds_userIdHeader() {
        validation.addSession("token", "mmuster");
        MockServerHttpRequest request = MockServerHttpRequest.get("/songs").header("Authorization", "token").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = authFilter.apply(new SongFilter.Config());
        filter.filter(exchange, gatewayFilterChain);
        assertEquals("mmuster", captor.getValue().getRequest().getHeaders().getFirst("userId-Header"));
    }
}
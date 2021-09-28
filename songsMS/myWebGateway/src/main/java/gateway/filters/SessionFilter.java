package gateway.filters;

import gateway.service.Validation;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SessionFilter extends AbstractGatewayFilterFactory<SessionFilter.Config> {

    private Validation valid;

    public SessionFilter(Validation validation) {
        super(SessionFilter.Config.class);
        this.valid = validation;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            if (response.getStatusCode().is2xxSuccessful()) {
                String authHeader = response.getHeaders().getFirst("USERID");
                String token = response.getHeaders().getFirst("TOKEN");
                valid.addSession(token, authHeader);
            }
        }));
    }

    public static class Config {

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

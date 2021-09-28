package gateway.filters;

import gateway.service.Validation;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class SongFilter extends AbstractGatewayFilterFactory<SongFilter.Config> {

    private Validation valid;

    public SongFilter(Validation validation) {
        super(Config.class);
        this.valid = validation;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String user = valid.getUserId(authHeader);
            ServerHttpRequest shr = request.mutate().header("userId-Header", user).build();
            return chain.filter(exchange.mutate().request(shr).build());
        };
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

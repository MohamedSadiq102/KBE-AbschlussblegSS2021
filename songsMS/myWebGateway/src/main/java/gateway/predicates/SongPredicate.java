package gateway.predicates;

import gateway.service.Validation;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

@Component
public class SongPredicate extends AbstractRoutePredicateFactory<SongPredicate.Config> {

    private Validation valid;

    public SongPredicate(Validation validation) {
        super(Config.class);
        this.valid = validation;
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return (ServerWebExchange t) -> {
            HttpHeaders headers = t.getRequest().getHeaders();
            String auth = headers.getFirst(HttpHeaders.AUTHORIZATION);
            return auth != null && valid.validSession(auth);
        };
    }

    public static class Config {
        private String name;

//        public String getName() {
//            return this.name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }

    }
}

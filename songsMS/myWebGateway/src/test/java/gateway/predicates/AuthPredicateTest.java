package gateway.predicates;

import gateway.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class AuthPredicateTest {

    private Validation validation;
    private SongPredicate songPredicate;

    @BeforeEach
    void setUp() {
        validation = new Validation();
        songPredicate = new SongPredicate(validation);
    }

    @Test
    void apply_with_exisiting_session_accepts_request() {
        validation.addSession("token", "mmuster");
        Predicate<ServerWebExchange> p = songPredicate.apply(new SongPredicate.Config());
        assertTrue(p.test(MockServerWebExchange.from(MockServerHttpRequest.get("/songs").header("Authorization", "token").build())));
    }

    @Test
    void apply_with_not_exisiting_session_denies_request() {
        Predicate<ServerWebExchange> p = songPredicate.apply(new SongPredicate.Config());
        assertFalse(p.test(MockServerWebExchange.from(MockServerHttpRequest.get("/songs").header("Authorization", "token").build())));
    }
}
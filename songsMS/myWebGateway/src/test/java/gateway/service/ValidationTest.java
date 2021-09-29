package gateway.service;

import gateway.GatewayApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Validation validation;

    @BeforeEach
    void setUp() {
        validation = new Validation();
    }

    @Test
    void session_is_known_after_addSession() {
        validation.addSession("token", "mmuster");
        assertTrue(validation.validSession("token"));
    }

    @Test
    void getUserId_returns_right_id() {
        validation.addSession("token", "mmuster");
        assertEquals(validation.getUserId("token"), "mmuster");
    }

    @Test
    void validSession_before_any_session_added_is_invalid() {
        assertFalse(validation.validSession("token"));
    }

    @Test
    public void main() {
        GatewayApp.main(new String[] {});
    }
}
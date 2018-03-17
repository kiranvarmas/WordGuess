package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SpellCheckServiceTest {

    SpellCheckService webservice;

    @Test
    void canary() {
        assertTrue(true);
    }

    @BeforeEach
    void setup() {
        webservice = new SpellCheckService();
    }

    @Test
    void checkIfWordIsValid() {
        assertTrue(webservice.isSpellingCorrect("hello"));
    }

    @Test
    void checkIfWordIsInValid() {
        assertEquals(false, webservice.isSpellingCorrect("fdfd"));
    }

    @Test
    void checkForNetworkFailureWithAStub() throws IOException {
        String message = "Network Failure";

        SpellCheckService service = spy(SpellCheckService.class);
        when(service.getResponseFromURL(anyString())).thenThrow(new IOException(message));

        assertThrows(RuntimeException.class, () -> service.isSpellingCorrect("whatever"), message);
    }


}


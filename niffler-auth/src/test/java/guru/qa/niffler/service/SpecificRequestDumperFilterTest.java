package guru.qa.niffler.service;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecificRequestDumperFilterTest {

    private SpecificRequestDumperFilter specificRequestDumperFilter;

    @Test
    void doFilterRequestIsHttpServletRequestTest(@Mock ServletResponse response,
                                                 @Mock FilterChain chain,
                                                 @Mock GenericFilter decorate,
                                                 @Mock HttpServletRequest hRequest) throws ServletException, IOException {
        String urlPattern = "testUrl";
        when(hRequest.getRequestURI()).thenReturn(urlPattern);
        specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPattern);
        specificRequestDumperFilter.doFilter(hRequest, response, chain);
        verify(decorate, times(1)).doFilter(hRequest, response, chain);
    }

    @Test
    void doFilterRequestNoHttpServletRequestTest(@Mock ServletRequest request,
                                                 @Mock ServletResponse response,
                                                 @Mock FilterChain chain,
                                                 @Mock GenericFilter decorate) throws ServletException, IOException {
        String[] urlPatterns = Arrays.array("testUrl");
        specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPatterns);
        specificRequestDumperFilter.doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(eq(request), eq(response));
    }

    @Test
    void doFilterRequestHttpServletRequestWithoutPatternTest(@Mock ServletResponse response,
                                                             @Mock FilterChain chain,
                                                             @Mock GenericFilter decorate,
                                                             @Mock HttpServletRequest hRequest) throws ServletException, IOException {
        specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate);
        specificRequestDumperFilter.doFilter(hRequest, response, chain);
        verify(chain, times(1)).doFilter(hRequest, response);
        verify(decorate, times(0)).doFilter(hRequest, response, chain);
    }

    @Test
    void destroyTest(@Mock GenericFilter decorate) {
        String urlPatterns = "testUrl";
        specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate, urlPatterns);
        specificRequestDumperFilter.destroy();
        verify(decorate, times(1)).destroy();
    }
}
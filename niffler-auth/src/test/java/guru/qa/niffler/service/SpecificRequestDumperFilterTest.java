package guru.qa.niffler.service;

import jakarta.servlet.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class SpecificRequestDumperFilterTest {

    private SpecificRequestDumperFilter specificRequestDumperFilter;

//    @BeforeEach
//    void initMockFilter(@Mock SpecificRequestDumperFilter specificRequestDumperFilter) {
//        lenient().when(userRepository.findByUsername("correct"))
//                .thenReturn(testUserEntity);
//    }

    @Test
    void doFilter(@Mock ServletRequest request,
                  @Mock ServletResponse response,
                  @Mock FilterChain filterChain,
                  @Mock GenericFilter genericFilter) throws ServletException, IOException {
        String[] urlPatterns = {"dsd","dsdr"};
        specificRequestDumperFilter = new SpecificRequestDumperFilter(genericFilter, urlPatterns);
        specificRequestDumperFilter.doFilter(request, response, filterChain);
    }

    @Test
    void destroy() {

    }
}
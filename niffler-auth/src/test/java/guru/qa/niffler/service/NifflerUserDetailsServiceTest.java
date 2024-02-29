package guru.qa.niffler.service;

import guru.qa.niffler.data.Authority;
import guru.qa.niffler.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NifflerUserDetailsServiceTest {

  private NifflerUserDetailsService nifflerUserDetailsService;

  @BeforeEach
  void initMockRepository() {
    nifflerUserDetailsService = new NifflerUserDetailsService(new UserRepository.Fake());
  }

  @Test
  void loadUserByUsername() {
    final UserDetails correct = nifflerUserDetailsService.loadUserByUsername("correct");

    final List<SimpleGrantedAuthority> authorityEntities  = List.of(new SimpleGrantedAuthority(Authority.read.name())
            , new SimpleGrantedAuthority(Authority.write.name()));

    assertEquals(
        "correct",
        correct.getUsername()
    );
    assertEquals(
        "test-pass",
        correct.getPassword()
    );
    assertEquals(
        authorityEntities,
        correct.getAuthorities()
    );

    assertTrue(correct.isAccountNonExpired());
    assertTrue(correct.isAccountNonLocked());
    assertTrue(correct.isCredentialsNonExpired());
    assertTrue(correct.isEnabled());
  }

  @Test
  void loadUserByUsernameNegayive() {
    final UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> nifflerUserDetailsService.loadUserByUsername("incorrect")
    );

    assertEquals(
        "Username: incorrect not found",
        exception.getMessage()
    );
  }
}
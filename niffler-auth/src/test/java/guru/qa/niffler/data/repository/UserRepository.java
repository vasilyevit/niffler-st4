package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.Authority;
import guru.qa.niffler.data.AuthorityEntity;
import guru.qa.niffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    @Nullable
    UserEntity findByUsername(@Nonnull String username);

    final class Fake implements UserRepository {

        @Nullable
        @Override
        public UserEntity findByUsername(@Nonnull String username) {
            if (username.equals("incorrect")) {
                throw new UsernameNotFoundException("Username: " + username + " not found");
            }
            List<AuthorityEntity> authorityEntities;

            UserEntity userEntity = new UserEntity();
            AuthorityEntity read = new AuthorityEntity();
            read.setUser(userEntity);
            read.setAuthority(Authority.read);
            AuthorityEntity write = new AuthorityEntity();
            write.setUser(userEntity);
            write.setAuthority(Authority.write);
            authorityEntities = List.of(read, write);

            userEntity.setUsername("correct");
            userEntity.setAuthorities(authorityEntities);
            userEntity.setEnabled(true);
            userEntity.setPassword("test-pass");
            userEntity.setAccountNonExpired(true);
            userEntity.setAccountNonLocked(true);
            userEntity.setCredentialsNonExpired(true);
            userEntity.setId(UUID.randomUUID());

            return userEntity;
        }
    }
}

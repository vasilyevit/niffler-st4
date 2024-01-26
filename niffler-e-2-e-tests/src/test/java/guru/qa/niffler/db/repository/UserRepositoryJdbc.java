package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO \"authority\" " +
                   "(user_id, authority) " +
                   "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, CurrencyValues.RUB.name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public void deleteInAuthById(UUID id) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement userPs = conn.prepareStatement(
              "DELETE FROM \"user\" WHERE id = ? ;");
           PreparedStatement authorityPs = conn.prepareStatement(
                   "DELETE FROM \"authority\" where user_id = ? ;")
      ) {
        authorityPs.setObject(1, id);
        authorityPs.executeUpdate();
        userPs.setObject(1, id);
        userPs.executeUpdate();
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
              "DELETE FROM \"user\" WHERE id = ? ;")) {
        ps.setObject(1, id);
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateUser(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      PreparedStatement userPs = conn.prepareStatement("UPDATE \"user\" " +
              "SET password = ?, enabled = ?, account_non_expired = ?, " +
              "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?");
      userPs.setObject(1, pe.encode(user.getPassword()));
      userPs.setBoolean(2, user.getEnabled());
      userPs.setBoolean(3, user.getAccountNonExpired());
      userPs.setBoolean(4, user.getAccountNonLocked());
      userPs.setBoolean(5, user.getCredentialsNonExpired());
      userPs.setObject(6, user.getId());
      int rowsResult = userPs.executeUpdate();
      if (rowsResult == 0) {
        throw new IllegalStateException("Can`t find user with id " + user.getId());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserAuthEntity getUserFromAuthById(UUID userId) {
    UserAuthEntity userAuthEntity = new UserAuthEntity();
    try (Connection conn = authDs.getConnection()) {
      try (PreparedStatement authPs = conn.prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
        authPs.setObject(1, userId);
        ResultSet authRs = authPs.executeQuery();
        if (authRs.next()) {
          userAuthEntity.setId((UUID) authRs.getObject("id"));
          userAuthEntity.setUsername(authRs.getString("username"));
          userAuthEntity.setPassword(authRs.getString("password"));
          userAuthEntity.setEnabled(authRs.getBoolean("enabled"));
          userAuthEntity.setAccountNonExpired(authRs.getBoolean("account_non_expired"));
          userAuthEntity.setAccountNonLocked(authRs.getBoolean("account_non_locked"));
          userAuthEntity.setCredentialsNonExpired(authRs.getBoolean("credentials_non_expired"));
        } else {
          throw new IllegalArgumentException("Can`t find user with id " + userId );
        }
        return userAuthEntity;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}

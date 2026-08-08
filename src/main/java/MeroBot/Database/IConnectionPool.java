package MeroBot.Database;

import java.sql.Connection;
import java.sql.SQLException;

interface IConnectionPool{
  public Connection GetConnection() throws SQLException;
}

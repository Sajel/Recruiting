package ua.kpi.nc.persistence.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kpi.nc.persistence.dao.UserDao;
import ua.kpi.nc.persistence.model.Role;
import ua.kpi.nc.persistence.model.SocialInformation;
import ua.kpi.nc.persistence.model.User;
import ua.kpi.nc.persistence.model.impl.proxy.RoleProxy;
import ua.kpi.nc.persistence.model.impl.proxy.SocialInformationProxy;
import ua.kpi.nc.persistence.model.impl.real.UserImpl;
import ua.kpi.nc.persistence.util.JdbcTemplate;
import ua.kpi.nc.persistence.util.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chalienko on 13.04.2016.
 */
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class.getName());

    public UserDaoImpl(DataSource dataSource) {
        this.setJdbcTemplate(new JdbcTemplate(dataSource));
    }

    private static final String SQL_GET_BY_ID = "SELECT u.id, u.email, u.first_name,u.last_name, u.second_name, " +
            "u.password, u.confirm_token, u.is_active, u.registration_date\n" +
            "FROM \"user\" u\n" +
            "WHERE u.id = ?;";

    private static final String SQL_GET_BY_EMAIL = "SELECT u.id, u.email, u.first_name,u.last_name,u.second_name," +
            " u.password,u.confirm_token, u.is_active, u.registration_date\n" +
            "FROM \"user\" u\n" +
            " WHERE u.email = ?;";

    private static final String SQL_EXIST = "select exists(SELECT email from \"user\" where email =?);";

    private static final String SQL_INSERT = "INSERT INTO \"user\"(email, first_name," +
            " second_name, last_name, password, confirm_token, is_active, registration_date) " +
            "VALUES (?,?,?,?,?,?,?,?);";

    private static final String SQL_UPDATE = "UPDATE \"user\" SET email = ?, first_name  = ?," +
            " second_name = ?, last_name = ?, password = ?, confirm_token = ?, is_active = ?, registration_date = ?" +
            "WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM \"user\" WHERE \"user\".id = ?;";

    private static final String SQL_GET_ALL = "SELECT u.id, u.email, u.first_name,u.last_name,u.second_name," +
            " u.password,u.confirm_token, u.is_active, u.registration_date\n" +
            "FROM \"user\" u\n";

    @Override
    public User getByID(Long id) {
        if (log.isInfoEnabled()){
            log.info("Looking for user with id = " + id);
        }
        return this.getJdbcTemplate().queryWithParameters(SQL_GET_BY_ID, new UserExtractor(), id);
    }

    @Override
    public User getByUsername(String email) {
        if (log.isInfoEnabled()) {
            log.info("Looking for user with email = " + email);
        }
        return this.getJdbcTemplate().queryWithParameters(SQL_GET_BY_EMAIL, new UserExtractor(), email);
    }

    @Override
    public boolean isExist(String email) {
        int cnt = this.getJdbcTemplate().update(SQL_EXIST, email);
        return cnt > 0;
    }

    @Override
    public Long insertUser(User user, Connection connection) {
        if (log.isInfoEnabled()) {
            log.info("Insert user with email = " + user.getEmail());
        }
        return this.getJdbcTemplate().insert(SQL_INSERT,connection, user.getEmail(),user.getFirstName(),user.getSecondName(),
                user.getLastName(),user.getPassword(),user.getConfirmToken(),user.isActive(),user.getRegistrationDate());
    }
    @Override
    public int updateUser(User user){
        if (log.isInfoEnabled()) {
            log.info("Update user with id = " + user.getId());
        }
        return this.getJdbcTemplate().update(SQL_UPDATE, user.getEmail(),user.getFirstName(),user.getSecondName(),
                user.getLastName(),user.getPassword(),user.getConfirmToken(),user.isActive(),user.getRegistrationDate(),
                user.getId());
    }

    @Override
    public int deleteUser(User user) {
        if (log.isInfoEnabled()) {
            log.info("Delete user with id = " + user.getId());
        }
        return this.getJdbcTemplate().update(SQL_DELETE, user.getId());
    }

    @Override
    public boolean addRole(User user, Role role) {
        if ((user.getId() == null) &&(log.isDebugEnabled())) {
            log.warn("User: "+ user.getEmail() + " don`t have id");
            return false;
        }
        return this.getJdbcTemplate().insert("INSERT INTO \"user_role\"(id_user, id_role) VALUES (?,?)",
                user.getId(),role.getId()) > 0;
    }

    @Override
    public boolean addRole(User user, Role role, Connection connection) {
        if ((user.getId() == null) &&(log.isDebugEnabled())) {
            log.warn("User: "+ user.getEmail() + " don`t have id");
            return false;
        }
        return this.getJdbcTemplate().insert("INSERT INTO \"user_role\"(id_user, id_role) VALUES (?,?);",connection,
                user.getId(),role.getId()) > 0;
    }

    @Override
    public int deleteRole(User user, Role role) {
        if ((user.getId() == null) &&(log.isDebugEnabled())) {
            log.warn("User: "+ user.getEmail() + " don`t have id");
            return 0;
        }
        return this.getJdbcTemplate().update("DELETE FROM \"user_role\" WHERE id_user= ? AND " +
                "id_role = ?", user.getId(),role.getId());
    }

    @Override
    public Set<User> getAll(){
        if (log.isInfoEnabled()) {
            log.info("Get all Users");
        }
        return this.getJdbcTemplate().queryForSet(SQL_GET_ALL, new UserExtractor());
    }

    private Set<Role> getRoles(Long userID){
        return this.getJdbcTemplate().queryWithParameters("SELECT ur.id_role\n" +
                "FROM \"user_role\" ur\n" +
                "WHERE ur.id_user = ?;", resultSet -> {
                    Set<Role> roles = new HashSet<>();
                    do {
                        roles.add(new RoleProxy(resultSet.getLong("id_role")));
                    }while (resultSet.next());
                    return roles;
                },userID);
    }

    private Set<SocialInformation> getSocialInfomations(Long userID){
        return this.getJdbcTemplate().queryWithParameters("SELECT si.id\n" +
                "FROM \"social_information\" si\n" +
                "WHERE si.id_user = ?;", resultSet -> {
            Set<SocialInformation> set = new HashSet<>();
            do {
                set.add(new SocialInformationProxy(resultSet.getLong("id")));
            }while (resultSet.next());
            return set;
        },userID);
    }

    private final class UserExtractor implements ResultSetExtractor<User> {
        @Override
        public User extractData(ResultSet resultSet) throws SQLException {
            User user = new UserImpl();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setSecondName(resultSet.getString("second_name"));
            user.setPassword(resultSet.getString("password"));
            user.setConfirmToken(resultSet.getString("confirm_token"));
            user.setActive(resultSet.getBoolean("is_active"));
            user.setRegistrationDate(resultSet.getTimestamp("registration_date"));
            user.setRoles(getRoles(resultSet.getLong("id")));
            user.setSocialInformations(getSocialInfomations(resultSet.getLong("id")));
            return user;
        }
    }
}
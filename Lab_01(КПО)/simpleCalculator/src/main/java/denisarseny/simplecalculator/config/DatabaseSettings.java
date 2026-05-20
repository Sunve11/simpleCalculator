package denisarseny.simplecalculator.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Параметры БД (для калькулятора отключены; зарезервировано для расширения).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseSettings {

    private boolean enabled;
    private String jdbcUrl = "jdbc:sqlite:${user.home}/.simpleCalculator/calculator.db";
    private String username = "";
    private String password = "";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String resolveJdbcUrl() {
        return jdbcUrl.replace("${user.home}", System.getProperty("user.home"));
    }
}

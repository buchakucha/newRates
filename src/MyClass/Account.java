package MyClass;

public class Account {
    public String login;
    public String password;
    public float money;

    public Account(String login, String password, float money) {
        this.login = login;
        this.password = password;
        this.money = money;
    }

    public String getLogin() {
        return login;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public boolean checkNewLogin(String loginInput) {
        if (loginInput.equals(login)) return false;
        else return true;
    }

    public boolean checkLoginAndPassword(String loginInput, String passwordInput) {
        if ((loginInput.equals(login)) && (passwordInput.equals(password))) return true;
        else return false;
    }

    public boolean canPlaceBet(float moneyInput) {
        return (money - moneyInput) < 0 ? false : true;
    }

    public void addMoney(float add) {
        money += add;
    }

    public boolean isEnoughMoneyForMatch() {
        return money <= 0 ? false : true;
    }

    @Override
    public String toString() {
        return login + " " + password + " " + money;
    }
}

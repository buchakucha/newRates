package MyClass;

public class Account {
    public String login;
    public String password;
    public float money;

    private boolean canParticipate = false;

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

    public boolean checkNewLogin(String loginInput) {
        if (loginInput.equals(login)) return false;
        else return true;
    }

    public boolean checkLoginAndPassword(String loginInput, String passwordInput) {
        if ((loginInput.equals(login)) && (passwordInput.equals(password))) return true;
        else return false;
    }

    public void placeBet(float moneyInput) {
        canParticipate = false;
        if ((money - moneyInput) < 0) {
            System.out.println("На вашем счету недостаточно средств. Введите сумму не более " + getMoney());
        } else {
            canParticipate = true;
            money -= moneyInput;
            System.out.println("Отлично! На вашем счету " + getMoney() + " монет");
        }
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

    public boolean isCanParticipate() {
        return canParticipate;
    }

    public void setCanParticipate(boolean canParticipate) {
        this.canParticipate = canParticipate;
    }
}

package main.java;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

public class Bank {

    public static final int AMOUNT_FOR_CHECK = 50000;
    private final Map<String, Account> accounts;
    private final Random random = new Random();

    public final DecimalFormat NUMBER_FORMAT = new DecimalFormat( "###,###" );

    public Bank(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {

        Account from = getAccountValue(fromAccountNum);
        Account to = getAccountValue(toAccountNum);
        if ((from != null && to != null) && (from != to)) {
            synchronized (from.compareTo(to) > 0 ? from : to) {
                synchronized (from.compareTo(to) > 0 ? to : from) {
                    if (!from.isBlock() && !to.isBlock()) {
                        try {
                            if (amount > AMOUNT_FOR_CHECK && isFraud(fromAccountNum, toAccountNum, amount)) {
                                from.setBlock(true);
                                to.setBlock(true);
                                System.out.println("Transfer from " + fromAccountNum + " to " + toAccountNum
                                        + " with amount " + amount
                                        + "\nFraud operation. Transaction blocked!\n");
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (from.getMoney() < amount) return;
                        from.setMoney(getBalance(fromAccountNum) - amount);
                        to.setMoney(getBalance(toAccountNum) + amount);
                        System.out.println("Transfer from " + fromAccountNum + " to " + toAccountNum
                                + " with amount " + amount
                                + "\nBalance for account " + fromAccountNum + " is: "
                                + NUMBER_FORMAT.format(getBalance(String.valueOf(fromAccountNum)))
                                + "\nBalance for account " + toAccountNum + " is: "
                                + NUMBER_FORMAT.format(getBalance(String.valueOf(toAccountNum))) + '\n');
                    } else {
                        System.out.println("Transaction with blocked account(s).\n"
                                + "From " + fromAccountNum + " (block status: " + from.isBlock() + ")"
                                + " to " + toAccountNum + " (block status: " + to.isBlock() + ")"
                                + " with amount " + amount
                                + "\nTransaction canceled!\n");
                    }
                }
            }
        }

    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return getAccountValue(accountNum)
                .getMoney();
    }

    public long getSumAllAccounts() {
        return accounts
                .entrySet()
                .parallelStream()
                .mapToLong(acc -> acc.getValue().getMoney())
                .sum();
    }

    private Account getAccountValue(String accountNum) {
        return accounts
                .entrySet()
                .parallelStream()
                .filter(acc -> acc.getValue().getAccNumber().equals(accountNum))
                .findAny()
                .get()
                .getValue();
    }

}

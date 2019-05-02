package chapter03;

import java.util.Scanner;

/**
 * @author binvi
 * @version 1.0
 * @Description:
 * @date 2019/5/2 21:12
 */
public class Retirement2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("How much money do you need to retire?");
        double goal = in.nextDouble();

        System.out.println("How much money will you contribute every year?");
        double payment = in.nextDouble();

        System.out.println("Interest rate in %: ");
        double interestRate = in.nextDouble();

        double balance = 0;
        int years = 0;

        String input;

        do {
            balance += payment;
            double interest = balance * interestRate / 100;
            balance += interest;
            years++;

            System.out.printf("After year %d, your balance is %,.2f%n", years, balance);

            System.out.println("Ready to retire? (Y/N)");
            input = in.next();
        } while (input.equals("N"));
    }
}

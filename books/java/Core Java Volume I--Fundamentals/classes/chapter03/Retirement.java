package chapter03;

import java.util.Scanner;

/**
 * @author binvi
 * @version 1.0
 * @Description:
 * @date 2019/5/2 21:08
 */
public class Retirement {
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

        while (balance < goal) {
            balance += payment;
            double interest = balance * interestRate / 100;
            balance += interest;
            years++;
        }

        System.out.println("You can retire in " + years + " years");
    }
}

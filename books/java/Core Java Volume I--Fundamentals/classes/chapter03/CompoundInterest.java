package chapter03;

/**
 * @author binvi
 * @version 1.0
 * @Description: Growth of an Investment at Different Interest Rates
 * @date 2019/5/2 20:36
 */
public class CompoundInterest {
    public static void main(String[] args) {
        final double STARTRATE = 10;
        final int NRATES = 6;
        final int NYEARS = 10;

        // set interest rates to 10... 15%
        double[] interestRate = new double[NRATES];
        for (int i = 0; i < interestRate.length; i++) {
            interestRate[i] = (STARTRATE + i) / 100.0;
        }

        double[][] balances = new double[NYEARS][NRATES];
        // set initial balances to 10000
        for (int i = 0; i < balances[0].length; i++) {
            balances[0][i] = 10000;
        }

        // compute interest for future years
        for (int i = 1; i < balances.length; i++) {
            for (int j = 0; j < balances[i].length; j++) {
                // get last year's balances from previous row
                double oldBalance = balances[i - 1][j];
                // compute interest
                double interest = oldBalance * interestRate[j];
                // compute this year's balances
                balances[i][j] = oldBalance + interest;
            }
        }

        // print one row of interest rates
        for (int j = 0; j < interestRate.length; j++) {
            System.out.printf("%9.0f%%", 100 * interestRate[j]);
        }

        System.out.println();

        // print balance table
        for (double[] row : balances) {
            // print table row
            for (double balance : row) {
                System.out.printf("%10.2f", balance);
            }
            System.out.println();
        }

    }

}

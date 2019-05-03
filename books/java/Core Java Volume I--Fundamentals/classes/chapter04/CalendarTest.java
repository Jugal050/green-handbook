package chapter04;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author binvi
 * @version 1.0
 * @Description: display a calendar for the current month
 *
 *      Mon Tue Wed Thu Fri Sat Sun
 *
 *              1   2   3*  4   5
 *      6   7   8   9   10  11  12
 *      13  14  15  16  17  18  19
 *      20  21  22  23  24  25  26
 *      27  28  29  30  31
 *
 * @date 2019/5/3 14:57
 */
public class CalendarTest {

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int today = date.getDayOfMonth();

        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
        date = date.minusDays(today - 1);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        for (int i = 1; i < dayOfWeek.getValue(); i++) {
            System.out.print("    ");
        }
        while (date.getMonthValue() == month) {
            System.out.printf("%3d", date.getDayOfMonth());
            if (date.getDayOfMonth() == today) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1) {
                System.out.println();
            }
        }
        if (date.getDayOfWeek().getValue() != 1) {
            System.out.println();
        }
    }
}

package chapter03;

/**
 * @author binvi
 * @version 1.0
 * @Description:
 * @date 2019/5/2 21:03
 */
public class Constants {
    public static void main(String[] args) {
        final double CM_PER_INCH = 2.54;
        double paperWidth = 8.5;
        double paperHeight = 11;
        System.out.println("Page size in centimeters: "
                + paperWidth * CM_PER_INCH + " by " + paperHeight * CM_PER_INCH);
        String all = String.join("-", "I", "am", "Iron", "man");
        System.out.println(all);
    }
}

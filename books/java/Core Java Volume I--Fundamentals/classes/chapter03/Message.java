package chapter03;

/**
 * @author binvi
 * @version 1.0
 * @Description: main method's String[] args
 * @date 2019/5/2 20:52
 */
public class Message {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h")) {
            System.out.print("Hello,");
        } else if (args[0].equals("-g")) {
            System.out.println("Goodbye,");
        }
        for (int i = 1; i < args.length; i++) {
            System.out.print(" " + args[i]);
        }
        System.out.print("!");

    }

}

import java.awt.*;
import java.awt.event.KeyEvent;

public class PreventScreenLock {

	public static void main(String[] args) {
		System.out.println("Prevent Screen Lock Start");
		try {
			while (true) {
				System.out.println("Press Key Scroll Lock");
				Robot robot = new Robot();		
				robot.keyPress(KeyEvent.VK_SCROLL_LOCK);
				robot.keyRelease(KeyEvent.VK_SCROLL_LOCK);
				Thread.currentThread().sleep(60000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}	
	}
}
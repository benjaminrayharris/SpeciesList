// Author: Benjamin Harris
// entry point for the SpeciesList program
// April 6, 2020

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SLMain {
	
	public static void main(String[] args) {
		SLEngine eng = new SLEngine();
		JFrame win = new JFrame("Species List Manager v" + eng.SLVERSION);
		String exitQ = "Are you sure you want to exit?";
		win.setMinimumSize(new Dimension(800, 600));
		win.add(eng);
		//win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		win.addWindowListener(
			new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) { 
					boolean result = UtilityShed.ask(exitQ);
					if (result) System.exit(0);
				}
			}
		);
		win.getContentPane();
		win.pack();
		win.setVisible(true);
		win.setLocationRelativeTo(null);
		win.setIconImage(eng.sbsImage);
	} // end main()
} // end class SLMain

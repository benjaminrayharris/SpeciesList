// Author: Ben Harris
// utility class provides various helper functions
// April 20, 2020

//import java.io.IOException;
//import java.io.FileNotFoundException;
//import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

final public class UtilityShed {
	
	public static void popup (String msg, ImageIcon icon) {
		JOptionPane.showMessageDialog(null, msg, "Alert!", JOptionPane.INFORMATION_MESSAGE, icon);
	} // end popup()
	
	public static boolean ask (String qstn) {
		int opt = JOptionPane.showConfirmDialog(null, qstn, "???????", JOptionPane.YES_NO_OPTION);
		return (opt == JOptionPane.YES_OPTION);
	} // end ask()
	
	public static String getin (String prmpt) {
		return JOptionPane.showInputDialog(prmpt);
	} // end getin()

	public static InputStream loadFile (String path) {
		InputStream in = UtilityShed.class.getResourceAsStream(path);
		if (in == null) in = UtilityShed.class.getResourceAsStream("/" + path);
		return in;
	} // end loadFile()
	
	// readFile() return value is non-null for success, null for failure
	public static String readFile (String path, CustomFilter filter, boolean chooser) {
		String strToParse = "";
		File file;
		try {
			if (!chooser) {
				if (path == null) {
					popup("debug readFile(): 'path' can't be null", null); //////////////////////////
					return null;
				} // end if
				file = new File(path);
			} else {
				JFileChooser fc = new JFileChooser((path == null ? "ezezez" : path));
				fc.setFileFilter(filter); // filter can be 'null'
				fc.setAcceptAllFileFilterUsed(true);
				int result = fc.showDialog(null, "Load File");
				if (result == JFileChooser.APPROVE_OPTION) file = fc.getSelectedFile();
				else {
					popup("file load cancelled", null); /////////////////////////////////////////////////////////////////////////////
					return null;
				} // end if-else
			} // end if-else
			if (!file.exists()) {
				//popup(file.getName() + " does not exist", null);
				return null;
			} // end if
			BufferedReader in = new BufferedReader( new FileReader(file));
			String str = in.readLine();
			strToParse = str;
			str = in.readLine();
			while (str != null) {
				strToParse += "\n" + str;
				str = in.readLine();
			} // end while
			in.close();
		} catch (Exception e) { 
			popup("debug readFile()    file: " + path + "\n\n" + e, null);
		} // end try-catch
		return strToParse;
	} // end readFile()

	// saveFile() return value is null for success, string message for failure reason
 	public static String saveFile (String st, String path, CustomFilter filter, boolean chooser) {
 		File file;
		try {
			if (!chooser) {
				if (path == null) {
					return "save was aborted - no file path"; ////////////////////
				} // end if
				file = new File(path);
			} else {
				JFileChooser fc = new JFileChooser((path == null ? "ezezez" : path));
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(true);
				int result = fc.showDialog(null, "Save File");
				if ((result == JFileChooser.APPROVE_OPTION)) {
					String di = fc.getSelectedFile().getParentFile().toString();
					String name = fc.getSelectedFile().getName();
					String ext = fc.getFileFilter().getDescription();
					if (ext.equals("All Files")) ext = "";
					else ext = ext.substring(1);
					if (!name.endsWith(ext)) name = name + ext;
					name = di + File.separator + name;
					file = new File(name);
					popup("File saved as:  " + name, null);
				} else return "save was cancelled";
			} // end if-else
			if (file.getParentFile().exists()) {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				out.println(st);
				out.close();
			} else {
				popup("debug saveFile(): file's parents doesn't exist", null);
				return "problem with existence of file's 'parent'";
			} // end if-else
		} catch (Exception e) { 
			popup("debug saveFile(): " + e, null);
			return "problem in 'try': " + e;
		} // end try-catch
		return null;
	} // end saveFile()
	
} // end class UtilityShed

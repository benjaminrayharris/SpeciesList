// Author: Benjamin Harris
// engine for SpeciesList program
// April 7, 2020

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;

public class SLEngine extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1035287244006253731L;
	private String dirPath, mlPath, epPath, csvPath, exPath, newLine, udPath;
	//private String dirDaddyPath, spPath;
	private JPanel lSideJP, buttonAreaJP, rSideJP, listJP, listContJP, buttonGroupJP;
	private JRadioButton[] sortRB;
	private ButtonGroup bg;
	private JLabel picJL;
	private JScrollPane listJSP;
	private JButton setUsualThreshJB, exportExcelJB, exportCsvJB, clearAllJB, checkAllJB;
	private JButton loadNewMasterJB, showAboutJB, updateJB;
	private JCheckBox showAllJCB, showUsualJCB;
	private WidgetList widgets;
	private String[] header;
	private int usualThresh;
	private int sortIndex;
	private int prntIndex;
	private boolean showAll, showUsual;
	public BufferedImage sbsImage;
	public ImageIcon ii;
	public boolean screenAdds;
	public static int[] colWid;
	public static int rowHei;
	public static int prntCol;
	public static int sortCol;
	public static int uniqCol;
	public static String[] dd1;
	public static String[] dd2;
	public final int SLVERSION = 2;
	public static final int scrollSpeed = 10;
	public static final int X1 = 200;
	public static final int X2 = 1900;
	public static final int Y = 1300;

	public SLEngine () {
		setImg();
		setDir();
		setVars();
		setPrefs();
		makeMasterlist(true);
		setPanels();
	} // end constructor SLEngine()

	private void pop (String s) {
		UtilityShed.popup(s,  ii);
	} // end pop()

	private boolean ask (String qstn) {
		return UtilityShed.ask(qstn);
	} // end ask()

	private String get (String prmpt) {
		return UtilityShed.getin(prmpt);
	} // end get()

	private void setImg () {
		final String imageFile = "sbs.png";
		try {
			sbsImage = ImageIO.read(UtilityShed.loadFile(imageFile));
			ii = new ImageIcon(sbsImage);
		} catch (Exception e) {
			UtilityShed.popup("setImg(): " + e, null);
		} // end try-catch
	} // end setImg()

	private void setDir () {
		File f = null;
		//File df = null;
		try {
			f = new File(SLEngine.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
			dirPath = f.toString();
			//df = f.getParentFile();
			//dirDaddyPath = df.toString();
			//pop(dirPath);
		} catch (Exception e) {
			pop("setDir(): " + e);
		} // end try-catch
	} // end setDir()

	private void setVars () {
		final String mlFile = "masterlist.cssl";
		final String csvFile = "SList.out.csv";
		final String exFile = "SLMacro.xlsm";
		final String udFile = "slu.jar";
		final String epFile = "primary.entry.pref";
		//final String spFile = "primary.settings.pref";
		mlPath = dirPath + File.separator + mlFile;
		csvPath = dirPath + File.separator + csvFile;
		exPath = dirPath + File.separator + exFile;
		udPath = dirPath + File.separator + udFile;
		epPath = dirPath + File.separator + epFile;
		//spPath = dirPath + File.separator + spFile;
		newLine = System.getProperty("line.separator");
		listContJP = new JPanel();
		widgets = new WidgetList();
		header = new String[0];
	} // end setVars()

	private void setPrefs () {
		showAll = true;
		showUsual = true;
		usualThresh = 3;
		sortIndex = 3;
		prntIndex = 1;
		screenAdds = true;
		dd1 = new String[]{"", "rock", "riparian"};
		dd2 = new String[]{"", "nox. weed", "rare"};
		colWid = new int[]{100, 300, 240, 120, 60, 190, 55, 70, 100, 100};
		rowHei = 14;
		prntCol = 4;
		sortCol = 4;
		uniqCol = 3;
	} // end setPrefs()

	private void makeMasterlist (boolean fromConstructor) {
		String[] fromGetFileString = getFileString((fromConstructor ? mlPath : null), new CSVFilter());
		if (!fromConstructor) {
			// ask to backup file at 'mlPath' first
		} // end if
		boolean internal = fromGetFileString[0] == null;
		String fileString = fromGetFileString[1];
		if (fileString != null) {
			screenAdds = !internal;
			String[] lines = fileString.trim().split("\n");
			setHeader(lines[0]);
			makeWidgets(lines, internal);
			if (!internal) saveMasterlist();
		} // end if
	} // end makeMasterlist()

	private String[] getFileString (String file, CustomFilter filter) {
		String[] rtrns = new String[2];
		rtrns[0] = null;
		boolean chooser = file == null;
		rtrns[1] = UtilityShed.readFile(file, null, chooser);
		if (rtrns[1] == null && !chooser) {
			pop(file + " does not exist.\nPlease choose a file for initial load...");
			rtrns[0] = "NOT INTERNAL";
			rtrns[1] = UtilityShed.readFile(file, filter, true);
		} // end if
		return rtrns;
	} // end getFileString()

	private void setHeader (String lines0) {
		try {
			header = parseCSVLine(lines0);
		} catch (Exception e) {
			pop("debug setHeader():\n" + e );
		} // end try-catch
	} // end setHeader()

	private String[] getEntryPrefs () {
		String[] lines = {""};
		String fileString = UtilityShed.readFile(epPath, null, false);
		if (fileString != null) {
			lines = fileString.trim().split("\n");
		} // end if
		return lines;
	} // end getEntryPrefs()

	private void makeWidgets (String[] lines, boolean internal) {
		EntryPref ep = new EntryPref();
		listContJP.removeAll();
		widgets = new WidgetList();
		String[] line;
		int usual;
		String dd1d;
		String dd2d;
		String[] vals;
		String[] epLines = getEntryPrefs();
		try {
			for (int k = 0; k < epLines.length; k++) {
				ep.add(parseCSVLine(epLines[k]));
			} // end for
			for (int i = 1; i < lines.length; i++) {
				int al;
				line = new String[header.length];
				String[] tmp = parseCSVLine(lines[i]);
				if (tmp.length > header.length) {
					pop("debug makeWidgets(): line bigger than header");
					return;
				} // end if
				al = header.length;
				for (int j = 0; j < al; j++) {
					line[j] = (j < tmp.length ? tmp[j] : "");
				} // end for
				vals = ep.getEntryPref(line[0], line[1], line[2], line[3]);
				usual = Integer.parseInt(vals[0]);
				dd1d = vals[1];
				dd2d = vals[2];
				widgets.addWidget(header, line, this, usual, dd1d, dd2d);
			} // end for
			widgets.sort(sortIndex);
			listContJP.add(widgets);
		} catch (Exception e) {
			pop("Problem loading masterlist in SLEngine.makeWidgets():\n" + e);
		} // try-catch
	} // end makeWidgets()

	public String[] parseCSVLine (String s) {
		String line = s;
		String[] vals;
		line = line.replaceAll("\r", " ");
		line = line.replaceAll("\t", " ");
		line = line.replaceAll(newLine, " ");
		line = line.trim();
		vals = line.split(",");
		for (int i = 0; i < vals.length; i++) vals[i] = vals[i].trim();
		return vals;
	} // end parseCSVLine()

	private void setPanels () {
		setWidgetList();

		// create this panel
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(new Dimension(X1 + X2 / 2, Y / 2));
		setPreferredSize(new Dimension(X1 + X2 / 2, Y / 2));
		setMaximumSize(new Dimension(X1 + X2, Y));
		Dimension dime = new Dimension(10, 10);
		add(Box.createRigidArea(dime));

		// create lSide panel
		lSideJP = new JPanel();
		Dimension budi = new Dimension(200, 950);
		lSideJP.setMaximumSize(budi);

		// create buttonArea panel
		buttonAreaJP = new JPanel();
		buttonAreaJP.setLayout(new BoxLayout(buttonAreaJP, BoxLayout.PAGE_AXIS));
		buttonAreaJP.add(Box.createRigidArea(dime));
		Dimension butt = new Dimension(200, 25);

		picJL = new JLabel(new ImageIcon(sbsImage));
		buttonAreaJP.add(picJL);

		buttonAreaJP.add(Box.createRigidArea(dime));

		showAllJCB = new JCheckBox("Show All Species");
		showAllJCB.setToolTipText("Check to display all species");
		showAllJCB.setSelected(showAll);
		showAllJCB.addActionListener(this);
		buttonAreaJP.add(showAllJCB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		showUsualJCB = new JCheckBox("Show Usual Species");
		showUsualJCB.setToolTipText("Check to display species that have been exported numerous times - as set by 'threshold' value");
		showUsualJCB.setSelected(showUsual);
		showUsualJCB.setEnabled(!showAll);
		showUsualJCB.addActionListener(this);
		buttonAreaJP.add(showUsualJCB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		setUsualThreshJB = new JButton("Usual Threshold: " + usualThresh);
		setUsualThreshJB.setToolTipText("Click to change threshold");
		setUsualThreshJB.setEnabled(!showAll);
		setUsualThreshJB.setMinimumSize(butt);
		setUsualThreshJB.setPreferredSize(butt);
		setUsualThreshJB.setMaximumSize(butt);
		setUsualThreshJB.addActionListener(this);
		buttonAreaJP.add(setUsualThreshJB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		exportExcelJB = new JButton("Export List to Excel");
		exportExcelJB.setToolTipText("Export the checked species to the Excel macro");
		exportExcelJB.setMinimumSize(butt);
		exportExcelJB.setPreferredSize(butt);
		exportExcelJB.setMaximumSize(butt);
		exportExcelJB.addActionListener(this);
		buttonAreaJP.add(exportExcelJB);

		buttonAreaJP.add(Box.createRigidArea(dime));
		
		// create buttonGroupJP panel
		buttonGroupJP = new JPanel();
		buttonGroupJP.setLayout(new BoxLayout(buttonGroupJP, BoxLayout.PAGE_AXIS));
		bg = new ButtonGroup();
		sortRB = new JRadioButton[sortCol];
		if (header.length >= sortCol) {
			for (int i = 0; i < sortCol; i++) {
				sortRB[i] = new JRadioButton("Sort by '" + header[i] + "'");
				sortRB[i].setSelected(i == sortIndex);
				sortRB[i].addActionListener(this);
				bg.add(sortRB[i]);
				buttonGroupJP.add(sortRB[i]);
			} // end for
		} // end if
		buttonAreaJP.add(buttonGroupJP);

		buttonAreaJP.add(Box.createRigidArea(dime));

		exportCsvJB = new JButton("Export List to CSV");
		exportCsvJB.setToolTipText("Export the checked species to a .csv file");
		exportCsvJB.setMinimumSize(butt);
		exportCsvJB.setPreferredSize(butt);
		exportCsvJB.setMaximumSize(butt);
		exportCsvJB.addActionListener(this);
		buttonAreaJP.add(exportCsvJB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		clearAllJB = new JButton("Clear List");
		clearAllJB.setToolTipText("Press to clear list and start over");
		clearAllJB.setMinimumSize(butt);
		clearAllJB.setPreferredSize(butt);
		clearAllJB.setMaximumSize(butt);
		clearAllJB.addActionListener(this);
		buttonAreaJP.add(clearAllJB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		checkAllJB = new JButton("Check All Visible");
		checkAllJB.setToolTipText("Press to check all displayed entries");
		checkAllJB.setMinimumSize(butt);
		checkAllJB.setPreferredSize(butt);
		checkAllJB.setMaximumSize(butt);
		checkAllJB.addActionListener(this);
		buttonAreaJP.add(checkAllJB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		loadNewMasterJB = new JButton("Load New Masterlist");
		loadNewMasterJB.setToolTipText("Press to discard current list and load new masterlist file");
		loadNewMasterJB.setMinimumSize(butt);
		loadNewMasterJB.setPreferredSize(butt);
		loadNewMasterJB.setMaximumSize(butt);
		loadNewMasterJB.addActionListener(this);
		buttonAreaJP.add(loadNewMasterJB);

		buttonAreaJP.add(Box.createRigidArea(dime));
		buttonAreaJP.add(Box.createRigidArea(dime));
		buttonAreaJP.add(Box.createRigidArea(dime));

		showAboutJB = new JButton("Show About");
		showAboutJB.setToolTipText("Press to show about the developer(s)");
		showAboutJB.setMinimumSize(butt);
		showAboutJB.setPreferredSize(butt);
		showAboutJB.setMaximumSize(butt);
		showAboutJB.addActionListener(this);
		buttonAreaJP.add(showAboutJB);

		buttonAreaJP.add(Box.createRigidArea(dime));

		updateJB = new JButton("Update " + (getUpdateButtonString()));
		updateJB.setToolTipText("Check if program is up to date");
		updateJB.setMinimumSize(butt);
		updateJB.setPreferredSize(butt);
		updateJB.setMaximumSize(butt);
		updateJB.addActionListener(this);
		buttonAreaJP.add(updateJB);

		buttonAreaJP.add(Box.createRigidArea(dime));
		// completed buttonAreaJP panel

		lSideJP.add(buttonAreaJP);
		// completed lSideJP panel

		add(lSideJP);

		add(Box.createRigidArea(dime));		

		// create rSideJP panel
		rSideJP = new JPanel();
		rSideJP.setLayout(new BoxLayout(rSideJP, BoxLayout.PAGE_AXIS));

		// create listJP panel
		listJP = new JPanel();
		listJP.setLayout(new BoxLayout(listJP, BoxLayout.PAGE_AXIS));
		listJP.add(listContJP);
		// completed listJP panel

		listJSP = new JScrollPane(listJP);
		listJSP.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
		listJSP.setWheelScrollingEnabled(true);

		//rSideJP.add(listJP);
		rSideJP.add(Box.createRigidArea(dime));
		rSideJP.add(listJSP);
		rSideJP.add(Box.createRigidArea(dime));
		rSideJP.add(Box.createVerticalGlue());
		// completed rSideJP panel

		add(rSideJP);
		add(Box.createRigidArea(dime));
		add(Box.createHorizontalGlue());
	} // end setPanels()

	private String getUpdateButtonString () {
		String str = "Program";
		String tmp = null;
		int ver = 0;
		try {
			tmp = checkVersion();
			if (tmp != null) {
				ver = Integer.parseInt(tmp);
			} else {
				return str;
			} // end if-else
			if (SLVERSION < ver) {
				str = "Is Available";	
			} // end if
		} catch (Exception e) {
			return str;
		} // end try-catch
		return str;
	} // end getUpdateButtonString()

	private String checkVersion () {
		String url = "https://github.com/benjaminrayharris/SpeciesList/raw/master/version";
		URL u;
		InputStream is = null;
		BufferedInputStream bis = null;
		BufferedReader d;
		String s = null;
		String str = null;
		try {
			u = new URL(url);
			is = u.openStream();
			bis = new BufferedInputStream(is);
			d = new BufferedReader(new InputStreamReader(bis));
			while ((s = d.readLine()) != null) {
				str = s;
			}  // end while
		} catch (EOFException eofe) {
			pop("checkVersion(): " + eofe);
		} catch (MalformedURLException mue) {
			pop("checkVersion(): " + mue);
		} catch (IOException ioe) {
			pop("checkVersion(): " + ioe);
		} finally {
			try {
				is.close();
			} catch (IOException ioe) {
			} // end try-catch
		} // end try-catch-catch-catch-finally
		return str;		
	} // end checkVersion()

	private void setWidgetList () {
		widgets.setWidgets(showAll, showUsual, usualThresh);
	} // end setWidgetList()

	public void actionPerformed (ActionEvent e) {
		Widget w = null;
		Object src = e.getSource();
		if (src == showAllJCB) {
			if (showAllJCB.isSelected()) {
				showAll = true;
				showUsualJCB.setEnabled(false);
				setUsualThreshJB.setEnabled(false);
			} else {
				showAll = false;
				showUsualJCB.setEnabled(true);
				setUsualThreshJB.setEnabled(true);
			} // end if-else
			setWidgetList();
		} else if (src == showUsualJCB) {
			if (showUsualJCB.isSelected()) {
				showUsual = true;
				setUsualThreshJB.setEnabled(true);
			} else {
				showUsual = false;
				setUsualThreshJB.setEnabled(false);
			} // end if-else
			setWidgetList();
		} else if (src == setUsualThreshJB) {
			changeUsualThresh();
		} else if (src == exportExcelJB) {
			saveUnitList(true);
			if (updateUsuals()) saveEntryPrefs();
			openExcelMacro();
		} else if (src == exportCsvJB) {
			saveUnitList(false);
			if (updateUsuals()) saveEntryPrefs();
		} else if (src == clearAllJB) {
			clearChecked();
		} else if (src == checkAllJB) {
			selectVisible();
		} else if (src == loadNewMasterJB) {
			makeMasterlist(false);
			setWidgetList();
		} else if (src == showAboutJB) {
			showAbout();
		} else if (src == updateJB) {
			updateProgram();
		} else {
			for (int i = 0; i < sortCol; i++) {
				if (src == sortRB[i]) {
					sortIndex = i;
					widgets.sort(sortIndex);
					setWidgetList();
					return;
				} // end if
			} // end for
			w = ((Widget)((AbstractButton)src).getParent());
			if (src == w.addToListJChB) {
				w.enableButtons();
			} // end if
		} // end if-elseif-elseif-elseif-elseif-elseif-elseif-elseif-elseif-elseif-else
	} // end actionPerformed()

	private void saveMasterlist () {
		saveList(true, false, mlPath, new CSVFilter(), false);
	} // end saveMasterlist()

	private void saveUnitList (boolean toExcel) {
		if (sortIndex != prntIndex && ask("Do you want to sort by '" + header[prntIndex] + "' first?")) {
			sortIndex = prntIndex;
			widgets.sort(sortIndex);
			sortRB[sortIndex].setSelected(true);
			setWidgetList();
		} // end if
		saveList(false, false, (toExcel ? csvPath : null), new CSVFilter(), !toExcel);
	} // end saveUnitList()

	private void saveEntryPrefs () {
		saveList(false, true, epPath, new EPrefFilter(), false);
	} // end saveEntryPrefs()

	private void saveList (boolean isMaster, boolean isEP, String path, CustomFilter filter, boolean chooser) {
		String result = UtilityShed.saveFile(makeFileString(isMaster, isEP), path, filter, chooser);
		if (result != null ) {
			pop("nothing was saved: " + result);
		} // end if
	} // end saveList()

	private void openExcelMacro () {
		try {
			Desktop.getDesktop().open(new File(exPath));
		} catch (Exception e) {
			pop("Excel macro not there: SList.out.csv created.\n\nPress 'Export List to CSV' to save with another name");
		} // end try-catch
	} // end openExcelMacro()

	private String makeFileString (boolean isMaster, boolean isEP) {
		String str = "";
		for (int i = 0; i < header.length; i++) {
			if ((isMaster || i < prntCol) && !isEP) {
				if (i > 0) str += ",";
				str += header[i];
			} // end if
		} // end for
		if (!isMaster && !isEP) str += ",Habitat,Status";
		int len = widgets.getLength();
		for (int i = 0; i < len; i++) {
			str += widgets.getString(i, isMaster, isEP, (str.length() > 3 ? false : true));
		} // end for
		return str;
	} // end makeFileString()

	private void changeUsualThresh () {
		String tmp;
		try {
			tmp = get("Change Usual Threshold (must be a number)");
			usualThresh = Integer.parseInt(tmp); 
		} catch (Exception e) {
			pop("wasn't a number - change aborted");
		} // end try-catch
		setUsualThreshJB.setText("Usual Threshold: " + usualThresh);
		setWidgetList();
	} // end changeUsualThresh()

	private boolean updateUsuals () {
		if (!ask("Do you want to update usual suspects?")) return false;
		int len = widgets.getLength();
		for (int i = 0; i < len; i++) {
			if (widgets.isChecked(i)) widgets.bumpUsual(i);
		} // end for
		return true;
	}  // end updateUsuals()

	private void selectVisible () {
		if (!ask("Are you sure you want to select all visible?")) return;
		widgets.checkVisible();
		//setWidgetList();
	} // end selectVisible()

	private void clearChecked () {
		if (!ask("Are you sure you want to un-check all?")) return;
		widgets.unSelectAll();
		//setWidgetList();
	} // end clearChecked()

	private void updateProgram () {
		String tmp = null;
		int ver = 0;
		String prmpt = "";
		String prmpt2 = "\n\nAny unsaved/unfinished work will be discarded.\n\nContinue anyway?";
		try {
			tmp = checkVersion();
			if (tmp != null) {
				ver = Integer.parseInt(tmp);
				if (SLVERSION < ver) {
					prmpt = "\n\nAn update is available.\n\nDo you want to update (overwrite) this program?";
					if (ask(prmpt) && ask(prmpt2)) openUpdater();
				} else {
					prmpt = "\n\nThis program is already the latest version.\n\nUpdate (overwrite) anyway?";
					if (ask(prmpt) && ask(prmpt2)) openUpdater();
				} // end if-else
			} else {
				prmpt = "\n\nCould not get latest version number.\n\nUpdate (overwrite) anyway?";
				if (ask(prmpt) && ask(prmpt2)) openUpdater();
			} // end if-else
		} catch (Exception e) {
			prmpt = "\n\nProblem with latest version number.\n\nUpdate (overwrite) anyway?";
			if (ask(prmpt) && ask(prmpt2)) openUpdater();
		} // end try-catch
	} // end updateProgram()
	
	private void openUpdater () {
		try {			
			Desktop.getDesktop().open(new File(udPath));
			System.exit(0);
		} catch (Exception e) {
			pop("openUpdater(): " + e);
		} // end try-catch-catch
	} // end openUpdater()

	private void showAbout () {
		String msg = "Ben Harris\nbenjaminharris.info\nbenjaminrayharris@gmail.com\n(541)621-0022";
		String title = "about the developers";
		int diatype = JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(null, msg, title, diatype, null);
	} // end showAbout()

} // end object class SLengine
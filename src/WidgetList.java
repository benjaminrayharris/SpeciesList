// Author: Benjamin Harris
// class blueprint for WidgetList JPanel object
// April 20, 2020

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class WidgetList extends JPanel {

	private static final long serialVersionUID = -1035984344006253731L;
	private Widget[] widgets;

	public WidgetList () {
		widgets = new Widget[0];
	} // end constructor WidgetList()

	public int getLength () {
		return widgets.length;
	} // end getLength()

	public void addWidget (String[] header, String[] vals, SLEngine eng, int usual, String dd1v, String dd2v) {
		if (eng.screenAdds && screenVals(vals)) return;
		Widget[] tmp = widgets;
		widgets = new Widget[tmp.length + 1];
		for (int i = 0; i < tmp.length; i++) widgets[i] = tmp[i];
		widgets[tmp.length] = new Widget(header, vals, eng, usual, dd1v, dd2v);
	} // end addWidget()

	private boolean screenVals (String[] vals) {
		if (widgets.length == 0) return false;
		int rtrn = 0;
		int indx = -1;
		String alert = " !!!!!!!!!!!!!!!!!!!!!!!! This message should only be displayed on initial data load !!!!!!!!!!!!!!!!!!!!!!!!!      \n\n\n";
		String stmnt = "                                Similar entry already exists:\n\n";
		String str1 = "\n\n                                Existing Entry:\n    ";
		String str2 = "\n\n                                New Entry:\n    ";
		String qstn = "\n\n\n                              Do you want to add the New Entry?";
		boolean found = false;
		boolean screened = false;
		for (int i = 0; i < widgets.length; i++) {
			rtrn = compareVals(vals, widgets[i].getValues());
			if (rtrn > 0) {
				indx = i;
				found = true;
				break;
			} // end if
		} // end for
		if (rtrn == SLEngine.sortCol) return true;
		if (found) {
			str1 += writeVals(widgets[indx].getValues());
			str2 += writeVals(vals);
			screened = !UtilityShed.ask(alert + stmnt + str1 + str2 + qstn);
		} // end if
		return screened;
	} // end screenVals()

	private int compareVals (String[] vals1, String[] vals2) {
		int counter = 0;
		for (int i = 0; i < SLEngine.sortCol; i++) {
			if ((i == SLEngine.uniqCol) && (counter == 0)) return 0;
			if (vals1[i].toLowerCase().equals(vals2[i].toLowerCase()) && !vals1[i].equals("")
					&& !vals1[i].toLowerCase().equals("unknown")) counter++;
		} // end for
		return counter;
	} // end compareVals()

	private String writeVals (String[] vals) {
		String str = "";
		for (int i = 0; i < SLEngine.sortCol; i++) {
			str += "  " + vals[i];
		} // end for
		return str;
	} // end writeVals()

	public void sort (int index) {
		Widget tmp;
		//int index = 3; // widget value index to sort widgetlist on
		for (int i = 0; i < widgets.length; i++) {
			for (int j = 0; j < widgets.length - 1; j++) {
				if (widgets[j].getValue(index).compareToIgnoreCase(widgets[j + 1].getValue(index)) > 0) {
					tmp = widgets[j];
					widgets[j] = widgets[j + 1];
					widgets[j + 1] = tmp;
				} // end if
			} // end for
		} // end for
	} // end sort()

 	public void setWidgets (boolean showAll, boolean showUsual, int usualThresh) {
		int ut = usualThresh;
		removeAll();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < widgets.length; i++) {
			add(widgets[i]);
			widgets[i].setVisible(showAll || widgets[i].isChecked() || (showUsual && widgets[i].getUsual() >= ut));
		} // end for
		add(Box.createVerticalGlue());
		revalidate();
		repaint();
	} // end setWidgets()
 	
 	public void checkVisible () {
 		for (int i = 0; i < widgets.length; i++) {
			if (widgets[i].isVisible()) {
				widgets[i].makeCheck(true);
				widgets[i].enableButtons();
			}
		} // end for
 	} // end checkVisible()

	public void unSelectAll () {
		for (int i = 0; i < widgets.length; i++) {
			widgets[i].makeCheck(false);
			widgets[i].enableButtons();
		} // end for
	} // end unSelect()

	public boolean isChecked (int index) {
		return widgets[index].isChecked();
	} // isChecked()

	public String getString (int index, boolean isMaster, boolean isEP, boolean isFirst) {
		return widgets[index].toString(isMaster, isEP, isFirst);
	} // end getString()

	public int getUsual (int index) {
		return widgets[index].getUsual();
	} // end getUsual()

	public void bumpUsual (int index) {
		widgets[index].bumpUsual();
	} // end bumpUsual()

} // end object class WidgetList

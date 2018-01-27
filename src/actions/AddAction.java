package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import addressBook.AddressBook;
import addressBook.BookProperties;

public class AddAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AddressBook adb;
	private BookProperties bp;

	public AddAction(AddressBook adb, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(text, icon);
		this.adb = adb;
		this.bp = adb.getBookProperties();
		putValue(SHORT_DESCRIPTION, desc);
		putValue(MNEMONIC_KEY, mnemonic);
	}
	
	public AddAction(AddressBook adb, String text, ImageIcon icon, String desc, Integer mnemonic, Integer mnemonic1) {
		super(text, icon);
		this.adb = adb;
		this.bp = adb.getBookProperties();
		putValue(SHORT_DESCRIPTION, desc);
		putValue(MNEMONIC_KEY, mnemonic);
		Object keyS = KeyStroke.getKeyStroke(mnemonic, mnemonic1);
		putValue(ACCELERATOR_KEY, keyS);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		try {
			String key = JOptionPane.showInputDialog("Entrez le nom souhaité");
			String value = JOptionPane.showInputDialog("Entrez les informations");
			bp.addProperties(key, value);
			adb.activeSave();
		} catch (NullPointerException e2) {}
	}

}
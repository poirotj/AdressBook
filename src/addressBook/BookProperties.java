package addressBook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;

public class BookProperties {

	private final String PROP_FILE_LOCATION = "myProperties.properties";
	private Properties contacts;
	DefaultListModel<String> nameList = new DefaultListModel<String>();
	
	public BookProperties() {
		this.contacts = new Properties();
		this.initList();
	}
	
	private void initList() {
		try (InputStream in = new FileInputStream(this.PROP_FILE_LOCATION)) {

			this.contacts.load(in);
			
			for (Object string : contacts.keySet()) {
				nameList.addElement((String) string);
			}
			
		} catch (NullPointerException e) {
			try {
				this.saveFile();
				initList();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sort();
	}
	
	private boolean existsProperty(String key) {
		try {
			if (contacts.getProperty(key) != null)
				return true;
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}
	
	//  ----------------   get, set and public method   -----------------
	public Properties getContacts() {
		return contacts;
	}

	public void setContacts(Properties contacts) {
		this.contacts = contacts;
	}

	public DefaultListModel<String> getNameList() {
		return nameList;
	}

	public void setNameList(DefaultListModel<String> nameList) {
		this.nameList = nameList;
		sort();
	}
	
	public String getPropertyValue(String key) {
		String prop = contacts.getProperty(key);
		return (prop == null)? "" : prop;
	}
	
	public boolean setProperties(String key, String value) {
		if (!existsProperty(key))
			return false;
		contacts.setProperty(key, value);
		return true;
	}
	
	public boolean addProperties(String key, String value) {
		if(nameList.contains(key) && existsProperty(key))
			return false;
		if(key.length() > 0 && value.length() >0 ) {
			contacts.setProperty(key, value);
			nameList.addElement(key);
			sort();
			return true;
		}
		return false;
	}
	
	public void saveFile() {
		try (OutputStream out = new FileOutputStream(this.PROP_FILE_LOCATION)) {
			contacts.store(out, "mes contacts");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DefaultListModel<String> sort() {
		List<String> tempList = new ArrayList();
		String[] tableau = new String[nameList.getSize()];
		nameList.copyInto(tableau);
		nameList.clear();
		for (String string : tableau) {
			tempList.add(string);
		}
		Collections.sort(tempList);
		for (String string : tempList) {
			nameList.addElement(string);
		}
		return nameList;
	}
	
	public boolean deletProperties(String key) {
		if (!existsProperty(key))
			return false;
		int index = nameList.indexOf(key);
		nameList.removeElementAt(index);
		contacts.remove(key);
		return true;
	}

}
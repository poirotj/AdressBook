package addressBook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import actions.AddAction;
import actions.DellAction;
import actions.SaveAction;

public class AddressBook extends JFrame {

	private static final long serialVersionUID = 1L;

	private BookProperties adbP;
	private JList<String> listAdress;
	private JTextPane textPan = new JTextPane();
	private CaretListener caretListener;
	private int curLength;
	private boolean isSavable = false;
	private boolean closeWindows = true;
	private JPopupMenu popupMenu;
	
	protected Map<String, Action> action = new HashMap<>();

	private ImageIcon iconeSave = new ImageIcon("lib/toolbarButtonGraphics/general/Save16.gif");
	private ImageIcon iconeDell = new ImageIcon("lib/toolbarButtonGraphics/general/Delete16.gif");
	private ImageIcon iconeAdd = new ImageIcon("lib/toolbarButtonGraphics/general/Add16.gif");

	public AddressBook() {
		adbP = new BookProperties();
		listAdress = new JList<>(adbP.getNameList());
		
		this.addActionJList();
		this.loadActions();
		this.initMenu();
		this.initPopupMenu();
		this.initWindows();
		textPan.setEditable(false);
	}
	
	private void initPopupMenu() {
		popupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem(action.get("dell"));
		popupMenu.add(item);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem;
		JMenu menu;
		menu = new JMenu("Fichier");
		menu.setMnemonic(KeyEvent.VK_A);
		
		menuItem = new JMenuItem(action.get("save"));
		menu.add(menuItem);
		menuBar.add(menu);
		
		menu = new JMenu("Contacts");
		menu.setMnemonic(KeyEvent.VK_A);
		
		menuItem = new JMenuItem(action.get("add"));
		menu.add(menuItem);
		menuItem = new JMenuItem(action.get("dell"));
		menu.add(menuItem);
		
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	@SuppressWarnings("deprecation")
	private void loadActions() {
		SaveAction save = new SaveAction(this, "Enregistrer", iconeSave, "Save file", new Integer(KeyEvent.VK_S),
				new Integer(ActionEvent.CTRL_MASK));
		AddAction add = new AddAction(this, "Ajouter", iconeAdd, "Add element", new Integer(KeyEvent.VK_A),
				new Integer(ActionEvent.ALT_MASK));
		DellAction dell = new DellAction(this, "Supprimer", iconeDell, "Dellet element", new Integer(KeyEvent.VK_P));

		action.put("save", save);
		action.put("add", add);
		action.put("dell", dell);
	}

	private void addActionJList() {
		
		caretListener = new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				int length = textPan.getText().length();
				String curText = textPan.getText();
				if(length != curLength) {
					adbP.setProperties(getSelectedProperties(), curText);
					activeSave();
				}
			}
		};
		
		listAdress.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				textPan.removeCaretListener(caretListener);
				curLength = 0;
				@SuppressWarnings("rawtypes")
				JList o = (JList) e.getSource();
				try {
					String key = o.getSelectedValue().toString();
					String value = adbP.getPropertyValue(key);
					textPan.setText(value);
				} catch (NullPointerException e2) {
					textPan.setText(" ");
				}
				
				int index = listAdress.getSelectedIndex();
				if(index == -1) {
					textPan.setEditable(false);
					desactiveDelet();
				}else { 
					textPan.setEditable(true);
					activeDelet();
				}
			}
		});
		
		listAdress.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int buttonDown = e.getButton();
				if(buttonDown == MouseEvent.BUTTON3) {  // clic droit
					int index = listAdress.locationToIndex(e.getPoint());
					listAdress.setSelectedIndex(index);
					activePopupMenu(e);
				}
			}
		});
		
		textPan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				curLength = textPan.getText().length();
				textPan.addCaretListener(caretListener);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(isSavable == true) {
					int result = JOptionPane.showConfirmDialog(null, "voulez vous enregistre");
					if(result == 0) {
						adbP.saveFile();
					}
					if(result == 2) {
						closeWindows = false;
						System.exit(0);
					}
				}
				if(closeWindows == true)
					System.exit(0);
				closeWindows = true;
			}
		});
	}

	public void initWindows() {
		int width = 400;
		int height = 330;

		this.setTitle("Le titre");
		this.setSize(width, height);
		this.setLocation(200, 200);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBackground(Color.gray);
		initLayout();
		this.setVisible(true);
	}

	
	private void initLayout() {
		BorderLayout layout = new BorderLayout();
		this.setTitle("BorderLayout");
		getContentPane().setLayout(layout);
		
		JPanel pancenter = new JPanel();
		pancenter.setLayout(new BorderLayout());
		textPan.setPreferredSize(new Dimension(textPan.getWidth(), 70));
		JScrollPane scrollPane = new JScrollPane(listAdress);
		JScrollPane scrollPane2 = new JScrollPane(textPan);
		pancenter.add(scrollPane, BorderLayout.CENTER);
		pancenter.add(scrollPane2,BorderLayout.SOUTH);
		this.add(pancenter, BorderLayout.CENTER);

		JPanel fixToolbar = new JPanel();
		JButton save = new JButton(action.get("save"));
		JButton add = new JButton(action.get("add"));
		JButton dell = new JButton(action.get("dell"));
		fixToolbar.add(save);
		fixToolbar.add(add);
		fixToolbar.add(dell);
		
		this.add(fixToolbar, BorderLayout.SOUTH);

	}

	public BookProperties getBookProperties() {
		return this.adbP;
	}

	public Action getAction(String value) {
		return action.get(value);
	}
	
	public String getSelectedProperties() {
		return listAdress.getSelectedValue();
	}

	public void activeSave() {
		isSavable = true;
		action.get("save").setEnabled(true);
	}
	
	public void desactiveSave() {
		isSavable = false;
		action.get("save").setEnabled(false);
	}
	
	public void activeDelet() {
		isSavable = true;
		action.get("dell").setEnabled(true);
	}
	
	public void desactiveDelet() {
		isSavable = false;
		action.get("dell").setEnabled(false);
	}

	public void activePopupMenu(MouseEvent e) {
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
}

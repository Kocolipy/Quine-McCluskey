package qm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class GUI extends JFrame implements ActionListener{
	JTextField mMinTerms;
	JTextField mDontCares;
	JTextField mSize;
	JTextArea mOutput;
	ButtonGroup mGroup;
	JButton mSimplify;
	
	public GUI(){
		super("Quine-McCluskey Boolean Simplier");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,225);
        add(createInputPanel(), BorderLayout.NORTH);
        add(createOutputPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.EAST);
        super.getRootPane().setDefaultButton(mSimplify);
	}
	private JPanel createInputPanel() {
	    JPanel input = new JPanel();
        addBorder(input,"Input");
        input.setLayout(new FlowLayout(FlowLayout.CENTER,-5,3));
        
        JPanel minterms = new JPanel();
	    TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Minterms");
	    title.setTitlePosition(TitledBorder.TOP);
	    title.setTitleFont(new Font("Dialog", Font.ITALIC, 12));
	    minterms.setBorder(title);
        mMinTerms = new JTextField(17);
        minterms.add(mMinTerms);
        
        JPanel dontCare = new JPanel();
	    TitledBorder title1 = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Don't Cares");
	    title1.setTitlePosition(TitledBorder.TOP);
	    title1.setTitleFont(new Font("Dialog", Font.ITALIC, 12));
	    dontCare.setBorder(title1);
	    mDontCares = new JTextField(17);
        dontCare.add(mDontCares);
        
        JPanel sizePanel = new JPanel();
	    TitledBorder title2 = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Size");
	    title2.setTitlePosition(TitledBorder.TOP);
	    title2.setTitleFont(new Font("Dialog", Font.ITALIC, 12));
	    sizePanel.setBorder(title2);
	    mSize = new JTextField(3);
	    sizePanel.add(mSize);
        
        input.add(minterms);
        input.add(dontCare);
        input.add(sizePanel);
        return input; 
	}
	private JPanel createOutputPanel() {
	    JPanel input = new JPanel();
	    input.setLayout(new BorderLayout(0,0));
        addBorder(input,"Output");
        mOutput= new JTextArea(20, 3);
        mOutput.setBackground(input.getBackground());
        mOutput.setEditable(false);
        mOutput.setText("\n Input Minterms and Don't Cares separated by commas");
        mOutput.setLineWrap(true);
        mOutput.setFont(new Font("Dialog",Font.PLAIN, 15));
        input.add(mOutput);
        return input; 
	}
	private JPanel createControlPanel() {
	    JPanel input = new JPanel();
	    GroupLayout gLayout = new GroupLayout(input);
	    input.setLayout(gLayout);
	    ParallelGroup hGroup = gLayout.createParallelGroup();
	    gLayout.setHorizontalGroup(hGroup);
	    SequentialGroup vGroup = gLayout.createSequentialGroup();
	    gLayout.setVerticalGroup(vGroup);

	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), " ");
	    title.setTitleFont(new Font("Dialog", Font.ITALIC, 6));
	    panel.setBorder(title);
	    	    
        JLabel labelA = new JLabel("  A is the");
        panel.add(labelA, BorderLayout.NORTH);

        JRadioButton msbButton = new JRadioButton("MSB");
        msbButton.setActionCommand("MSB");
        msbButton.setSelected(true);

        JRadioButton lsbButton = new JRadioButton("LSB");
        lsbButton.setActionCommand("LSB");
        
        mGroup = new ButtonGroup();
        mGroup.add(msbButton);
        mGroup.add(lsbButton);
        
        panel.add(msbButton, BorderLayout.WEST);
        panel.add(lsbButton, BorderLayout.EAST);
        hGroup.addComponent(panel);
        vGroup.addComponent(panel, GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
        
  
        JPanel btmPanel = new JPanel();
        mSimplify = new JButton("Simplify");
        mSimplify.addActionListener(this);        
        btmPanel.add(mSimplify);
        hGroup.addComponent(btmPanel);
        vGroup.addComponent(btmPanel, GroupLayout.PREFERRED_SIZE,
                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
        return input; 
	}
	private void addBorder(JComponent component, String title) {
	    Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	    Border tb = BorderFactory.createTitledBorder(etch,title);
	    component.setBorder(tb);
	}
	public void actionPerformed(ActionEvent e) {
		String minterms = mMinTerms.getText().replaceAll(" ", ",");
		String dontcare = mDontCares.getText().replaceAll(" ", ",");
		String size = mSize.getText();
		int qmSize = 0;
		boolean passed = true;
		
		ButtonModel selectedSB = mGroup.getSelection();
		
		ArrayList<Integer> intList = new ArrayList<>();
		ArrayList<Integer> dontCaresList = new ArrayList<>();
		try{
			mOutput.setText("");
			for(String a : minterms.split(",")){
				if (!a.equals("")){
					if (Integer.parseInt(a) < 0){mOutput.setText("\n Minterms can only take positive values"); passed = false;}
					if (!intList.contains(Integer.parseInt(a))){
						intList.add(Integer.parseInt(a));
					}
				}
			}
			for(String a : dontcare.split(",")){
				if (!a.equals("")){
					if (Integer.parseInt(a) < 0){mOutput.setText("\n Don't Cares can only take positive values."); passed = false;}
					if (intList.contains(Integer.parseInt(a))){ mOutput.setText("\n Duplicates present in Minterms and Don't Cares"); passed = false;}
					if (!dontCaresList.contains(Integer.parseInt(a))){
						dontCaresList.add(Integer.parseInt(a));
					}
				}
			}
			qmSize = Integer.parseInt(size);
			if (qmSize <= 0){ mOutput.setText("\n Please input a positive size");  passed = false;}
			if (qmSize >= 27){ mOutput.setText("\n The simplifier is unable to resolve such a large size");  passed = false;}
		} catch (NumberFormatException e1){
			passed = false;
			mOutput.setText("\n Please input integers.");
		} 
		if (passed == true){
			Qm qm = new Qm(qmSize,intList, dontCaresList, selectedSB.getActionCommand().equals("MSB"));
			try {
				mOutput.setText("\n Solution: " + qm.calculate());
			} catch (IncorrectSizeException e1) {
				mOutput.setText("\n Ensure that size is large enough to cover all minterms");
			}
		}
	}
	public static void main(String[] args){
		GUI gui = new GUI();
        gui.setVisible(true);
	}
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.List;

public class ClassSelection extends JFrame {
	private JList<String> classList;
	private String[] classNames = {
			"GNEG 1111 - Introduction to Engineering I",
			"ENGL 1013 - Composition I", "CHEM 1103 - University Chemistry I",
			"MATH 2554 - Calculus I", "PHYS 2054 - University Physics I",
			"GNEG 1121 - Introduction to Engineering II", "MATH 2564 - Calculus II",
			"ENGL 1023 - Composition II", "CSCE 2004 - Programming Foundations I",
			"CSCE 2114 - Digital Design", "MATH 2603 - Discrete Mathematics",
			"CSCE 2014 - Programming Foundations II", "CSCE 2214 - Computer Organization",
			"MATH 3103 - Combinatorial and Discrete Mathematics", 
			"CSCE 3193 - Programming Paradigms", "CSCE 3613 Operating Systems",
			"COMM 1313 - Public Speaking", "MATH 3083 - Linear Algebra", 
			"INEG 2313 - Aplied Probablity and Statistics for Engineers I",
			"CSCE 3513 - Software Engineering", "CSCE 4523 Database Management Systems",
			"PHIL 3103 - Ethics and the Professions", "CSCE 4133 - Algorithms",
			"CSCE 4561 - Capstone I", "CSCE 4323 - Formal Languages and Computability",
			"CSCE 4963 - Capstone II"
	};
	private List<String> selectedItemsList = new ArrayList<String>();

	public ClassSelection() {
		super("Main Core Credit Received:");
		
		Container container = getContentPane();
		container.setLayout(new FlowLayout());
		
		classList = new JList<String>(classNames);
		classList.setVisibleRowCount(10);
		classList.setFixedCellHeight(45);
		classList.setFixedCellWidth(450);
		classList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		//Submit Button Actions
		JButton submit = new JButton("Submit");
		submit.addActionListener(event -> {
        	selectedItemsList = classList.getSelectedValuesList();
        	ArrayList<String> classesRemaining = classesRemaining();
        	String classesToTake = String.format("Looks like you still need to take %s.", classesRemaining);
			System.out.println(classesToTake);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dispose();
		});
		
		container.add(new JScrollPane(classList));
		container.add(submit);
		
		setSize(600, 600);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent event) {
	            dispose();
	         }     
	     });
	}
	
	//Method to calculate the classes remaining
	public ArrayList<String> classesRemaining() {
		ArrayList<String> classesRemaining = new ArrayList<String>();
		for (int i = 0; i < classNames.length; i++) {
			if (!(selectedItemsList.contains(classNames[i]))) {
				classesRemaining.add(classNames[i]);
			}
		}
		return classesRemaining; 
	}
}


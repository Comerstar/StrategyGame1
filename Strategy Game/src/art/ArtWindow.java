package art;

import javax.swing.JFrame;

public class ArtWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArtWindow()
	{
		System.out.println("windowMade");
		add(new ArtPanel());
		
		setResizable(true);
		
		setTitle("Testing Art Program");
		setLocationRelativeTo(null);
		setSize(600, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		//setFocusable(true);
	}
	
	public static void main(String[] args)
	{
		ArtWindow window = new ArtWindow();
		window.toFront();
	}
}

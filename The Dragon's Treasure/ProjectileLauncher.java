import javax.swing.JOptionPane;

public class ProjectileLauncher implements Runnable {
	private Model model;
	private View view;
	
	public ProjectileLauncher(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public synchronized void run() {
		while (model.getGameNotPaused()) {
			model.updateScene(view.getWidth(), view.getHeight());
			view.repaint();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
				JOptionPane.showMessageDialog(null, "Press R to resume game.");
			}
		}
	}
}

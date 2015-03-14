import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;

public class MapView extends JPanel {
	public MapView() {
		ImageIcon map = new ImageIcon("map.jpg");
		JLabel mapLabel = new JLabel(map);
		this.add(mapLabel,BorderLayout.CENTER);
	}
}

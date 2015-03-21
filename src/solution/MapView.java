package solution;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.awt.geom.*;
import java.awt.geom.*;
import scotlandyard.*;

public class MapView extends JPanel {
	public MapView() {
		URL u = this.getClass().getResource("map.jpg");
		ImageIcon map = new ImageIcon(u);
		JLabel mapLabel = new JLabel(map);
		this.add(mapLabel,BorderLayout.CENTER);
	}

	

	
}

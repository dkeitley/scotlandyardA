package solution;
import scotlandyard.*;
import java.util.*;

class TestClass {
	public static void main(String[] args) {
		//Values used only for test purposes. 
		List<Colour> colours = Arrays.asList(Colour.Black,Colour.Blue,
		Colour.Red,Colour.Green,Colour.White,Colour.Yellow,
		Colour.Yellow,Colour.Yellow,Colour.Yellow);

		GameView view = new GameView(30,colours);
		List<Boolean> testBools = Arrays.asList(false,false,false);
		try {
			ScotlandYardModel model = new ScotlandYardModel(1,testBools,"graph.txt");
			
			GameViewController presenter = new GameViewController(model,view);
		} catch(Exception e) {

		}
		//System.out.println(model.getPlayerLocation(Colour.Black));
		
		view.run();
		
	}
}

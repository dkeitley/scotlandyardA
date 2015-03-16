package solution;
import scotlandyard.*;
import java.util.*;

class TestClass {
	public static void main(String[] args) {
		//Values used only for test purposes. 
		List<Colour> colours = Arrays.asList(Colour.Black,Colour.Blue);
		Map<Colour,Integer> map = new HashMap<Colour,Integer>();
		map.put(Colour.Black,5);
		map.put(Colour.Blue,7);
		GameView view = new GameView(30,colours,map);
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

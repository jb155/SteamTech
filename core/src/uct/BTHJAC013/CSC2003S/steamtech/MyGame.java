package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;

	@Override
	public void create () {
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		setScreen(mainMenuScreen);
	}
}

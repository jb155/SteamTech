package uct.BTHJAC013.CSC2003S.steamtech.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uct.BTHJAC013.CSC2003S.steamtech.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 660;
		config.height = 350;
		config.resizable=false;
		config.title="SteamTech";

		new LwjglApplication(new MyGame(), config);
	}
}

package de.hetzge.sgame.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.event.EventStartGame;
import de.hetzge.sgame.game.event.setup.EventSetupPlayers;

public class ConnectGameGui extends Stage {

	private Button startButton;

	public void show() {
		addActor(startButton = new TextButton("Start", App.ressources.getSkin()) {
			{
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						App.network.send(new EventSetupPlayers(App.game.getPlayers()));
						App.network.sendAndSelf(new EventStartGame());
					}
				});
				setVisible(false);
			}
		});
	}

	public void showStartButton() {
		startButton.setVisible(true);
	}

}

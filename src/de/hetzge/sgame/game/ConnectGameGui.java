package de.hetzge.sgame.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hetzge.sgame.App;
import de.hetzge.sgame.game.event.EventStartGame;
import de.hetzge.sgame.game.event.setup.EventSetupPlayers;
import de.hetzge.sgame.network.NetworkModule;

public class ConnectGameGui extends Stage {

	private Button startButton;

	public void show() {
		addActor(this.startButton = new TextButton("Start", App.ressources.getSkin()) {
			{
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						NetworkModule.instance.send(new EventSetupPlayers(App.getGame().getPlayers()));
						NetworkModule.instance.sendAndSelf(new EventStartGame());
					}
				});
				setVisible(false);
			}
		});
	}

	public void showStartButton() {
		this.startButton.setVisible(true);
	}

}

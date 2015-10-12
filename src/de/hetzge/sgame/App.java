package de.hetzge.sgame;

import org.nustaq.serialization.FSTConfiguration;

import de.hetzge.sgame.frame.Timing;
import de.hetzge.sgame.function.EntityFunction;
import de.hetzge.sgame.function.Function;
import de.hetzge.sgame.function.RessourceFunction;
import de.hetzge.sgame.function.WorldFunction;
import de.hetzge.sgame.game.Game;
import de.hetzge.sgame.game.LibGdxApplication;
import de.hetzge.sgame.game.Updater;
import de.hetzge.sgame.graphic.Ressources;
import de.hetzge.sgame.network.Network;
import de.hetzge.sgame.render.EntityRenderer;
import de.hetzge.sgame.render.ItemRenderer;
import de.hetzge.sgame.render.Renderer;
import de.hetzge.sgame.render.WorldRenderer;
import de.hetzge.sgame.setting.Settings;

public final class App {

	public static final Timing timing = new Timing();
	public static final Ressources ressources = new Ressources();
	public static final Settings settings = new Settings();
	public static final Network network = new Network();
	public static final LibGdxApplication libGdxApplication = new LibGdxApplication();
	public static final Renderer renderer = new Renderer();
	public static final Updater updater = new Updater();

	public static final Function function = new Function();
	public static final RessourceFunction ressourceFunction = new RessourceFunction();
	public static final EntityFunction entityFunction = new EntityFunction();
	public static final WorldFunction worldFunction = new WorldFunction();

	public static final WorldRenderer worldRenderer = new WorldRenderer();
	public static final EntityRenderer entityRenderer = new EntityRenderer();
	public static final ItemRenderer itemRenderer = new ItemRenderer();

	public static final FSTConfiguration fstConfiguration = FSTConfiguration.getDefaultConfiguration();

	public static Game game;

	private App() {
	}
}

package de.hetzge.sgame.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;

public class LocalGameState {

	private final Set<Entity> selection = new HashSet<>();
	private E_EntityType entityTypeToBuild = null;

	private boolean showPaths = false;
	private boolean showCollisions = false;
	private boolean showRegistrations = false;
	private boolean showIds = false;

	public boolean hasSelection() {
		return !selection.isEmpty();
	}

	public void clearSelection() {
		selection.clear();
	}

	public Set<Entity> getSelection() {
		return selection;
	}

	public List<Entity> getSelection(Predicate<Entity> predicate) {
		return selection.stream().filter(predicate).collect(Collectors.toList());
	}

	public void addSelection(Entity entity) {
		selection.add(entity);
	}

	public void addSelection(List<Entity> entities) {
		selection.addAll(entities);
	}

	public void setSelection(List<Entity> entities) {
		clearSelection();
		selection.addAll(entities);
	}

	public int getSelectionSize() {
		return selection.size();
	}

	public boolean hasEntityTypeToBuild() {
		return entityTypeToBuild != null;
	}

	public E_EntityType getEntityTypeToBuild() {
		return entityTypeToBuild;
	}

	public void setEntityTypeToBuild(E_EntityType entityTypeToBuild) {
		clearSelection();
		this.entityTypeToBuild = entityTypeToBuild;
	}

	public boolean isShowPaths() {
		return showPaths;
	}

	public void setShowPaths(boolean showPaths) {
		this.showPaths = showPaths;
	}

	public void toggleShowPaths() {
		this.showPaths = !this.showPaths;
	}

	public boolean isShowCollisions() {
		return showCollisions;
	}

	public void setShowCollisions(boolean showCollisions) {
		this.showCollisions = showCollisions;
	}

	public void toggleShowCollision() {
		this.showCollisions = !this.showCollisions;
	}

	public boolean isShowRegistrations() {
		return showRegistrations;
	}

	public void setShowRegistrations(boolean showRegistrations) {
		this.showRegistrations = showRegistrations;
	}

	public void toggleShowRegistrations() {
		this.showRegistrations = !this.showRegistrations;
	}

	public boolean isShowIds() {
		return showIds;
	}

	public void setShowIds(boolean showIds) {
		this.showIds = showIds;
	}

	public void toggleShowIds() {
		this.showIds = !this.showIds;
	}

	public E_CommandMode getCommandMode() {
		if (hasSelection()) {
			return E_CommandMode.COMMAND;
		} else if (hasEntityTypeToBuild()) {
			return E_CommandMode.BUILD;
		} else {
			return E_CommandMode.NON;
		}
	}

}

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

	public void addSelection(Entity entity){
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

package graceyard;

public enum E_ContainerType {
	USE(0, 1), PROVIDES(1, 3), NEEDS(4, 3), HAS(7, 3);

	public static final E_ContainerType[] values = values();
	public static final int CONTAINER_SIZE = values[values.length - 1].FIRST_INDEX + values[values.length - 1].COUNT;

	public final int FIRST_INDEX;
	public final int COUNT;

	private E_ContainerType(int FIRST_INDEX, int COUNT) {
		this.FIRST_INDEX = FIRST_INDEX;
		this.COUNT = COUNT;
	}
}

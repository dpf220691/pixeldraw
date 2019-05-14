package bus;


/**
 *
 * @author dpf
 */
public abstract class Tool {

	public enum Arg {
		POINT, VALUE;
	}

	public abstract void input(Point arg);

	public abstract void input(double arg);

	public abstract Arg nextArg();

	public abstract boolean hasNextArg();

	public abstract String getNextMessage();

	public abstract String getName();

	public abstract void onSelect();

}

package co.uk.niadel.napi.util;

/**
 * Assorted methods that didn't really fit in the other utility classes.
 * @author Niadel
 *
 */
public final class UtilityMethods
{
	/**
	 * Converts 3 RGB values to a true RGB number. This means you don't have to spend ages calculating the RGB.
	 * @param red
	 * @param green
	 * @param blue
	 * @return The true RGB colour
	 */
	public static final long convertToRGBColour(int red, int green, int blue)
	{
		return ((red << 16) + (green << 8) + blue);
	}
}

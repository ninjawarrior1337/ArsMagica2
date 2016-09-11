package am2.api.flickers;

import com.google.common.collect.ImmutableList;

import am2.api.affinity.Affinity;

public interface IFlickerRegistry{
	/**
	 * Attempts to register a flicker operator into the flicker network
	 *
	 * @param singleton The instance of the class that should be called upon to perform the operations when loaded into a flicker block
	 * @param mask      The bit mask representing the flicker combination.  These are ORed affinity masks.  @see am2.api.spell.enums.Affinity.getMask for specific mask flags (flickers represent an affinity)
	 * @return True if registered, false if the mask is already in use.
	 */
	public boolean registerFlickerOperator(IFlickerFunctionality singleton, int mask);
	
	/**
	 * 
	 * @param singleton  The instance of the class that should be called upon to perform the operations when loaded into a flicker block
	 * @param affinities The affinities used by this focus
	 * @return True if registered, false if the mask is already in use.	 */
	public boolean registerFlickerOperator(IFlickerFunctionality singleton, Affinity... affinities);

	public int getMaskForOperator(IFlickerFunctionality operator);

	public IFlickerFunctionality getOperatorForMask(int mask);

	public int[] getMasks();

	public ImmutableList<IFlickerFunctionality> getAllOperators();
}

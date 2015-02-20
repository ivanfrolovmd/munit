package org.mule.munit;

public class CallCounter {
	private int count = 0;

	public int count() {
		count = count + 1;
//		System.out.println("************** lalalalalalalalal ******************");
		return count;
	}

	public int getCount() {
		return count;
	}

	public static CallCounter generate() {
		return new CallCounter();
	}

}

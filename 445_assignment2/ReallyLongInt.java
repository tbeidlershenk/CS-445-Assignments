// CS 0445 Spring 2022
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> 
							implements Comparable<ReallyLongInt>
{
	// Used to create a ReallyLongInt of value = 0
	private ReallyLongInt() {
		super();
		add(0);
	}

	// Constructor to parse a string argument into a ReallyLongInt
	public ReallyLongInt(String s){
		super();
		char c;
		int digit = -1;
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				if (!(digit == 0 && this.getLength() == 0)) 
					this.add(1, Integer.valueOf(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
		if (digit == 0 && this.getLength() == 0)
			this.add(1, Integer.valueOf(digit));
	}

	// Constructor to copy another ReallyLongInt
	public ReallyLongInt(ReallyLongInt rightOp) {
		super(rightOp);
	}

	// Constructor to parse a long argument into a ReallyLongInt
	public ReallyLongInt(long X) {
		if (X == 0) add(0);
		while (X > 0) {
			long temp = (X / 10) * 10;
			add(this.numberOfEntries+1, (int)(X - temp));
			X = temp / 10;
		}
	}

	// Overrides the toString method of LinkedListPlus
	public String toString() {
		Node curr = this.firstNode;
		String output = "";
		for (int i = 0; i < this.numberOfEntries; i++) {
			curr = curr.prev;
			output += curr.data.toString();
		}
		return output;
	}

	// Return new ReallyLongInt which is sum of current and argument
	public ReallyLongInt add(ReallyLongInt rightOp) {

		ReallyLongInt sum;
		Node curr1, curr2;
		int carry = 0;
		int smaller;

		// Setting sum = the larger of the two ReallyLongInts
		if (this.numberOfEntries > rightOp.numberOfEntries) {
			sum = new ReallyLongInt(this);
			curr2 = rightOp.firstNode;
			smaller = rightOp.numberOfEntries;
		} 
		else {
			sum = new ReallyLongInt(rightOp);
			curr2 = this.firstNode;
			smaller = this.numberOfEntries;
		}
		curr1 = sum.firstNode;

		// Adding the smaller ReallyLongInt to sum
		for (int i = 0; i < sum.numberOfEntries; i++) {
			if (i < smaller) {
				int val = curr1.data + curr2.data + carry;
				if (val < 10) {
					curr1.data = val;
					carry = 0;
				} else {
					curr1.data = val % 10;
					carry = 1;
				}
			} else if (carry == 1) {
				int val = curr1.data + carry;
				if (val < 10) {
					curr1.data = val;
					carry = 0;
					break;
				} else curr1.data = val % 10;
			} else break;
			curr1 = curr1.next;
			curr2 = curr2.next;
		}

		// If any carry digits are left over add another digit
		if (carry == 1) {
			sum.add(1);
		}
		return sum;
	}
	
	// Return new ReallyLongInt which is difference of current and argument
	public ReallyLongInt subtract(ReallyLongInt rightOp) {

		// If this < rightOp
		if (compareTo(rightOp) == -1) throw new ArithmeticException();

		ReallyLongInt diff = new ReallyLongInt(this);
		Node curr1 = diff.firstNode;
		Node curr2 = rightOp.firstNode;

		// Subtracting rightOp from this
		for (int i = 0; i < rightOp.numberOfEntries; i++) {
			if (curr1.data < curr2.data ) {
				int forward = 1;
				curr1 = curr1.next;
				while (curr1.data == 0) {
					curr1 = curr1.next;
					forward++;
				}
				curr1.data -= 1;
				curr1 = curr1.prev;
				forward--;
				while (curr1.data == 0 && forward > 0) {
					curr1.data += 9;
					curr1 = curr1.prev;
					forward--;
				}
				curr1.data += 10 - curr2.data;				
			} else curr1.data -= curr2.data;
			curr1 = curr1.next;
			curr2 = curr2.next;
		}
		// Removing insignificant zeroes
		curr1 = diff.firstNode.prev;
		while (curr1.data == 0 && diff.numberOfEntries > 1) {
			curr1 = curr1.prev;
			diff.numberOfEntries--;
		}
		diff.firstNode.prev = curr1;
		curr1.next = diff.firstNode;
		return diff;
	}

	// Multiplies this ReallyLongInt by rightOp
	public ReallyLongInt multiply(ReallyLongInt rightOp) {

		ReallyLongInt product = new ReallyLongInt();
		Node curr1 = this.firstNode;
		Node curr2 = rightOp.firstNode;
		int power = 0;

		for (int i = 0; i < this.numberOfEntries; i++) {
			int nextPow = power + 1;
			for (int k = 0; k < rightOp.numberOfEntries; k++) {
				ReallyLongInt temp = new ReallyLongInt(curr1.data * curr2.data);
				for (int j = 0; j < power; j++)
					if (!temp.toString().equals("0")) temp.add(1,0);
				product = product.add(temp);
				curr2 = curr2.next;
				power++;
			}
			power = nextPow;
			curr1 = curr1.next;
		}
		return product;
	}

	// Stub method for recursiveDivision, also checks the special cases
	public ReallyLongInt divide(ReallyLongInt divisor) {
		if (divisor.equals(new ReallyLongInt(0))) throw new ArithmeticException();
		if (this.equals(divisor)) return new ReallyLongInt(1);
		ReallyLongInt quotient = recursiveDivision(new ReallyLongInt(0), divisor);
		// Really should not have to have this line (180) but I kept running into errors without it
		if (compareTo(quotient.multiply(divisor)) == -1) quotient = quotient.subtract(new ReallyLongInt(1));
		return quotient;
	}

	// Recursive division method
		// Returns the quotient through a series of multiplications by some base (in this case base = 2)
		// Each recursive case finds the largest power of 2 that can be multiplied by the divisor and added 
		// to the quotient without exceeding the dividend
		// Returns the new value of the quotient with each recursive call
	private ReallyLongInt recursiveDivision(ReallyLongInt quotient, ReallyLongInt divisor) {
		
		ReallyLongInt prev = new ReallyLongInt(1);
		ReallyLongInt base = new ReallyLongInt(2);
		ReallyLongInt total = quotient.multiply(divisor);
		
		// Base case (if total >= dividend)
		if (compareTo(total.add(divisor)) != 1) 
			return new ReallyLongInt(1);
		// Recursive case
		while (compareTo(total.add(prev.multiply(base).multiply(divisor))) == 1) 
			prev = prev.multiply(base);
		return recursiveDivision(quotient.add(prev), divisor).add(prev);
		
	}

	// Return 1 if this > rOp, -1 if rOp > this, 0 if equal
	public int compareTo(ReallyLongInt rOp) {
		if (this.numberOfEntries > rOp.numberOfEntries) return 1;
		else if (this.numberOfEntries < rOp.numberOfEntries) return -1;
		Node curr1 = this.firstNode.prev;
		Node curr2 = rOp.firstNode.prev;
		for (int i = 0; i < this.numberOfEntries; i++) {
			if (curr1.data > curr2.data) return 1;
			else if (curr2.data > curr1.data) return -1;
			curr1 = curr1.prev;
			curr2 = curr2.prev;
		}
		return 0;
	}

	// Returns true if rightOp == this, false otherwise
	public boolean equals(Object rightOp) {
		ReallyLongInt copy = (ReallyLongInt) rightOp;
		if (this.numberOfEntries != copy.numberOfEntries) return false;
		Node curr1 = this.firstNode;
		Node curr2 = copy.firstNode;;
		for (int i = 0; i < this.numberOfEntries; i++) {
			if (curr1.data != curr2.data) return false;
			curr1 = curr1.next;
			curr2 = curr2.next;
		}
		return true;
	}
}

package v1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SDES
{
	// Stores the key for encryption
	private boolean [] key = new boolean [10];

	/**
	 * @author Dennis Klauder
	 * This will convert the Characters of the string into a array of bytes and
	 * then encrypts each letter
	 * 
	 * @param message
	 * @return ciphertext in a byte array
	 */
	public byte[] encrypt (String message) 
	{
		//check to make sure key is instantiated

		byte [] plainText = message.getBytes();
		byte [] cipherText = new byte [plainText.length];

		for (int i = 0; i < plainText.length; i++ )
		{
			cipherText[i] = encryptByte(plainText[i]);
		}

		return cipherText;
	}


	/**
	 * @author Dennis Klauder
	 * 
	 * @param a single byte to be encrypted
	 * @return the encrypted byte
	 */
	public byte encryptByte(byte b) 
	{
		//generate keys
		boolean [] key1 = expPerm(this.key,new int[] {0,6,8,3,7,2,9,5});
		boolean [] key2 = expPerm(this.key,new int[] {7,2,5,4,9,1,8,0});
		//convert to boolean array
		boolean [] bitArray = byteToBitArray(b,8);
		// initial permutation
		bitArray = expPerm(bitArray, new int[] {1,5,2,0, 3,7,4,6});
		//round 1
		bitArray = f(bitArray,key1);
		//switch nybbles
		bitArray = concat(rh(bitArray),lh(bitArray));
		//round 2
		bitArray = f(bitArray,key2);
		//inverse IP
		bitArray = expPerm(bitArray, new int[] {3,0,2,4, 6,1,7,5});

		byte cipherText = bitArrayToByte(bitArray);

		return cipherText;

	}

	/**
	 * @author Dennis Klauder
	 * 
	 * @param an encrypted cipher message in a byte []
	 * @return a byte array with the plaintext message information
	 */
	public byte[] decrypt (byte [ ] cipher)
	{
		byte [] cipherText = cipher;
		byte [] plainText = new byte [cipherText.length];

		for (int i = 0; i < cipherText.length; i++ )
		{
			plainText[i] = decryptByte(cipherText[i]);
		}

		return plainText;

	}

	/**
	 * @author Dennis Klauder
	 * @param a single encrypted byte
	 * @return a single byte of plaintext information
	 */
	public byte decryptByte (byte y) 
	{
		//generate keys
		boolean [] key1 = expPerm(this.key,new int[] {0,6,8,3,7,2,9,5});
		boolean [] key2 = expPerm(this.key,new int[] {7,2,5,4,9,1,8,0});
		//convert to boolean array
		boolean [] bitArray = byteToBitArray(y,8);
		// initial permutation
		bitArray = expPerm(bitArray, new int[] {1,5,2,0, 3,7,4,6});
		//round 2
		bitArray = f(bitArray,key2);
		//switch nybbles
		bitArray = concat(rh(bitArray),lh(bitArray));
		//round 1
		bitArray = f(bitArray,key1);
		//inverse IP
		bitArray = expPerm(bitArray, new int[] {3,0,2,4, 6,1,7,5});

		byte cipherText = bitArrayToByte(bitArray);

		return cipherText;
	}

	/**
	 * @author Dennis Klauder
	 * 
	 * This will display the contents of a byte array to the console
	 * @param byteArray
	 */
	public void show(byte [] byteArray) 
	{
		if(byteArray.length>0)
		{
			System.out.print(byteArray[0]);

			for (int i = 1; i < byteArray.length; i++) 
			{
				System.out.print(" " + byteArray[i]);
			}
		}
		System.out.println();
	}

	/**
	 * @author Dennis Klauder
	 * 
	 * This will show the contents of a boolean array as ones and zeros
	 * @param b
	 */
	public void show(boolean [] b) 
	{

		for (int i = 0; i < b.length; i++) 
		{
			if (b[i])
			{
				System.out.print("1");
			}
			else
			{
				System.out.print("0");
			}
			// puts a blank space after every 4th character
			if((i+1)%4==0)
			{
				System.out.print(" ");
			}
		}
		System.out.println();
	}




	/***
	 * @author Francis Fasola
	 * Converts a byte array into a String, containing the numerical values of the byte.
	 * @param inp An array of bytes.
	 * @return String representation of the array.
	 */
	public String byteArrayToString(byte [ ] inp) {
		String s = new String(inp);

		/**
		for (int i = 0; i < inp.length; i++)
			s += inp [i] + " ";
		 */

		return s;
	}

	/***
	 * @author Francis Fasola
	 * Converts an array of bits to a single byte.
	 * @param inp Array of booleans containing the values of the bits.
	 * @return byte representing the value of the bits.
	 */
	public byte bitArrayToByte(boolean [] inp) {			
		byte b = 0;
		// Check each value in the array and if the value is 1 (true) then shift. 
		// First shift by 7 and decrement each shift by 1
		for (int i = 0; i < inp.length; i++) {
			if (inp[i]) 
				b |= 1 << (inp.length-1 - i);
		}
		return b;
	}

	/***
	 * @author Francis Fasola
	 * Converts a byte into an array of bits of length size.
	 * @param b Byte to be converted.
	 * @param size Length of new array
	 * @return Array of bits of length size.
	 */
	public boolean [] byteToBitArray(byte b, int size) {
		boolean[] bits = new boolean[size];
		// Start at the right side (LSB) and shift to the left most position.
		// Perform an unsigned shift to the right and check if the value is -1
		// Then add 1, else add 0.
		for( int j = 0, i = size - 1; i >= 0; i--, j++) {
			byte c = (byte) (b << 7 - i);
			c = (byte) (c >>> 7);
			bits [j] = c == -1 ? true : false;
		}
		return bits;
	}

	/***
	 * @author Francis Fasola
	 * Concatenates two arrays of booleans together (x || y).
	 * @param x The left side of the new array.
	 * @param y The right side of the new array.
	 * @return The concatenation of x and y.
	 */
	public boolean [] concat(boolean[] x, boolean [] y) {
		boolean [] z = new boolean [x.length + y.length];
		int index = 0;
		// Cycle through x and y and add the values to z.
		for ( int i = 0; i < x.length; i++ ) {
			z[index] = x[i];
			index++;
		}
		for ( int i = 0; i < y.length; i++) {
			z[index] = y[i];
			index++;
		}
		return z;
	}

	/***
	 * @author Francis Fasola
	 * Permutes an array of bits. The bits are shuffled according to the values of epv. 
	 * Throws exception if the values of epv are greater than the length of inp.
	 * @param inp Array of bits to be permute.
	 * @param epv The values to change the bits of inp.
	 * @throws java.lang.IndexOutOfBoundsException
	 * @return Array of bits after the permutations.
	 */
	public boolean[] expPerm(boolean [] inp, int [] epv) throws java.lang.IndexOutOfBoundsException {
		boolean [] b = new boolean[epv.length];
		// Go through each value of epv and take the corresponding bit from inp
		// and store the value in b.
		for ( int i = 0; i < epv.length; i++) {
			b[i] = inp[epv[i]];
		}
		return b;
	}

	/**
	 * @author Ryan Hudson
	 * This method performs the rounding function.
	 * @param x - Boolean Array that represents some binary number.
	 * @param k - Boolean Array that represents some binary key.
	 * @return - Result of rounding as a Boolean Array representation of a binary number.
	 */
	public boolean [] f(boolean [] x, boolean [] k) {
		boolean result[];

		boolean feistel[];
		boolean xor[];

		feistel = feistel(k,rh(x));
		xor = xor(lh(x), feistel);
		result = concat(xor, rh(x));

		return result;
	}

	/**
	 * @author Ryan Hudson
	 * This method performs the feistel function.
	 * @param k - Boolean Array that represent a binary key.
	 * @param x - Boolean Array that represents some binary number.
	 * @return - Result of feistel as a Boolean Array representation of a binary number.
	 */
	public boolean [] feistel(boolean [] k, boolean [] x) {
		boolean result[]; //final result of feistel
		boolean left[]; //store the left half of x
		boolean right[]; //store the right half of x
		int s0Result; //value pulled out of S0-Box
		int s1Result; //value pulled out of S1-Box

		//S-Boxes stored as 2D-Arrays
		int[][] s0 = {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};	//S0 box
		int[][] s1 = {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};	//S1 box

		//permutation vectors needed for feistel
		int[] P4 = {1,3,2,0};	//permutes 4-bit string
		int[] EP = {3,0,1,2,1,2,3,0};	//Expands 4-bit String to permutable 8-bit String

		//XOR the key with the EP permutation on x.
		result = xor(k,expPerm(x,EP));

		//Grab left and right portions
		left = lh(result);
		right = rh(result);

		//This is where we grab the values from the S-Boxes, basically we need to grab the boolean values and use them to navigate the S-Box, 
		//in order to do this we need to do some conversion. Since left[] is a boolean array, we need to cast boolean to int: True to 1 and False to 0
		//(done with the code: boolean ? 1:0). After that we get turn the ints into Strings using String.valueOf(int) and concatenate them together to get the
		//binary number. Finally we convert the binary String back to an int using Integer.parseInt(String, 2), the 2 specifies the radix.
		s0Result = s0[Integer.parseInt(String.valueOf(left[0] ? 1:0) + String.valueOf(left[3] ? 1:0),2)]
				[Integer.parseInt(String.valueOf(left[1] ? 1:0) + String.valueOf(left[2] ? 1:0),2)];

		s1Result = s1[Integer.parseInt(String.valueOf(right[0] ? 1:0) + String.valueOf(right[3] ? 1:0),2)]
				[Integer.parseInt(String.valueOf(right[1] ? 1:0) + String.valueOf(right[2] ? 1:0),2)];

		//Now that we have the results of the S-Boxes as int's, we need to convert them back to boolean's so the can be stored in
		//a boolean array. Done with the intToBoolArray() method.
		left = intToBoolArray(s0Result);
		right = intToBoolArray(s1Result);	

		//P4 permutation on the concatenated boolean arrays.
		result = expPerm(concat(left,right), P4);

		return result;
	}

	/**
	 * @author Ryan Hudson
	 * This method takes and Integer and converts it into a Boolean Array by first converting the int to a binary String
	 * then seperating the 1's and 0's, then assigning True to 1 and False to 0.
	 * @param n - Integer that we want to convert to a boolean array.
	 * @return - Boolean array with the binary values of the Integer.
	 */
	public boolean[] intToBoolArray(int n)
	{
		String binary = String.format("%2s", Integer.toBinaryString(n)).replace(' ', '0');
		String parts[] = binary.split("");
		boolean result[] = new boolean[parts.length];

		for(int i=0; i < parts.length; i++) {
			if(parts[i].equals("1")) {
				result[i] = true;
			}
			else {
				result[i] = false;
			}
		}
		return result;
	}


	/***
	 * Takes input from the user and stores it in a bit array.
	 * @param scanner Scanner to get input from user.
	 */
	public void getKey10(java.util.Scanner scanner) {
		String s = scanner.next();
		char [] c = s.toCharArray();
		for ( int i = 0; i < c.length; i++) {
			key[i] = c[i] == '0' ? false : true;
		}
	}

	/**
	public void getKey10a(java.util.Scanner scanner) {
		String s = "";
		boolean isKeyValid = false;
		// Check the input entered valid if the data is 1 or 0
		// and it is 10 digits long
		while(!isKeyValid) {
			System.out.println("Please enter a key of 1 or 0 that is 10 digits long.");
			s = scanner.next();
			String regularExpression = "^[0-1]*$";
			Pattern pattern = Pattern.compile(regularExpression);
			Matcher matcher = pattern.matcher(s); 
			isKeyValid = matcher.matches();
			if(!isKeyValid || s.length() != 10) {
				isKeyValid = false;
				System.out.println("Invalid Key");
			}
		}
		char [] c = s.toCharArray();
		for ( int i = 0; i < c.length; i++) {
			key[i] = c[i] == '0' ? false : true;
		}
	}
	 */

	/***
	 * @author Francis Fasola
	 * Returns the left half of an array of bits. 
	 * @param inp Array of bits.
	 * @return The left side of the array.
	 */
	public boolean[] lh(boolean [] inp) {
		int half = inp.length / 2;
		boolean [] left = new boolean [half];
		for ( int i = 0; i < half; i++) {
			left[i] = inp [i];
		}
		return left;
	}
	/***
	 * @author Francis Fasola
	 * Returns the right half of an array of bits.
	 * @param inp Array of bits.
	 * @return The right side of the array.
	 */	
	public boolean[] rh(boolean [] inp) {
		int half = inp.length / 2;
		boolean [] right = new boolean [half];
		for ( int f = (half)-1, i = inp.length - 1; i >= half; i--, f--) {
			right[f] = inp [i];
		}
		return right;
	}

	/***
	 * @author Francis Fasola
	 * Exclusive OR both bit arrays and returns a new bit array with the result.
	 * Length of x and y must be the same.
	 * @param x The first array.
	 * @param y The second array.
	 * @return A new array with the value of x XOR y.
	 */
	public boolean [] xor(boolean [] x, boolean [] y) {
		boolean [] z = new boolean [x.length];

		for (int i = 0; i < z.length; i++) {
			z [i] = x [i] ^ y [i];
		}

		return z;
	}

	/***
	 * @author Francis Fasola
	 * Goes through the bit array and returns a String representation of 1 and 0.
	 * @param b Array of bits.
	 * @return String of 1 and 0.
	 */
	public String toString(boolean[] b) {
		String s = "";
		for (int i = 0; i < b.length; i++) {
			if (b[i])
				s = s + 1;
			else
				s = s + 0;
		}
		return s;
	}


	public void testKey10(String keyString) {
		String s = keyString;
		char [] c = s.toCharArray();
		for ( int i = 0; i < c.length; i++) {
			key[i] = c[i] == '0' ? false : true;
		}
	}
}
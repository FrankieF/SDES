package v1;
//import java.nio.charset.Charset;
//import java.util.*;
///**
// * Test the implementation of SDES
// * 
// * @author (sdb) 
// * @version (Sep 2010)
// */
//public class Driver
//{
//   public static void main(String[] args)
//{   
//	SDES sdes = new SDES();
////	boolean[] a = new boolean[] { true, true, false, false } ;
////	
////	boolean[] b = new boolean[] { true, false, true, false } ;
////	
////	int [] i = new int[] {3,1,2,0};
////	
//	byte[] d = new byte [] { 1, 1, 1, 1};
////	
////	boolean[] c = sdes.concat(a, b);
////	byte[] by = new byte[3];
////	by[0] = 1;
////	by[1] = -125;
////	by[2] = 63;
////	//c = sdes.xor(a,b);
////	String str = new String(d);System.out.println(str);
////	System.out.println("str " + str);
////	boolean[] nn = new boolean [] {false,false,false,false,false,false,false,false};
////	byte n = sdes.bitArrayToByte(nn);
////	System.out.println(sdes.toString(c));
////	System.out.println(n);
////	System.out.println(sdes.byteArrayToString(d));
//    Scanner scanner = new Scanner (System.in);
//    sdes.show(d);
//    
//    String plain = "x";
//    System.out.println ("Enter plain text, or hit 'Enter' to terminate");
//    plain = scanner.nextLine();
//    byte [] cipher;
//    while (plain.length() > 0)
//    {   
//        cipher = sdes.encrypt  (plain);
//        System.out.print ("Cipher is ");
//        sdes.show (cipher);
//        System.out.println (sdes.byteArrayToString (sdes.decrypt (cipher)));
//        System.out.println ("Enter plain text, or hit 'Enter' to terminate");
//        plain = scanner.nextLine();
//    }
//}
//}

import java.util.*;
/**
 * Test the implementation of SDES
 * 
 * @author (sdb) 
 * @version (Sep 2010)
 */
public class Driver
{
   public static void main(String[] args)
{   SDES sdes = new SDES();

    Scanner scanner = new Scanner (System.in);

    String plain = "x";
    System.out.println ("Enter plain text, or hit 'Enter' to terminate");
        plain = scanner.nextLine();
    byte [] cipher;
    while (plain.length() > 0)
    {   
        cipher = sdes.encrypt  (plain);
        System.out.print ("Cipher is ");
        sdes.show (cipher);
        System.out.println (sdes.byteArrayToString (sdes.decrypt (cipher)));
        System.out.println ("Enter plain text, or hit 'Enter' to terminate");
        plain = scanner.nextLine();
    }
}
}
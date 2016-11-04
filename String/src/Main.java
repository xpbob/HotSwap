import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class Main {
/*
	 public String str = "abc";
	   public  String str1 = "a";
	   public  String str2 = "bc";
	   public  String combo = str1+str2;
	     */
	/*
	 public String str;
	   public  String str1 ;
	   public  String str2 ;
	   public  String combo ;
	   public Main(){
		   str = "abc";
		   str1 = "a";
		   str2 = "bc";
		   combo = str1+str2;
	   }*/
	   
	    public void test(){
			  String str1 = "a";
			  String str2 = "bc";
			  String combo = str1+str2;
	        System.out.println(combo == "abc");
	    }
	 
	    public static void main(String args[]){
	     //   new Main().test();
	    	/*
	    	  String str = "abc";
	          String str1 = "a";
	          String str2 = "bc";
	          String combo = str1 + str2;
	          String combo2 = "a" + "bc";
	          //System.out.println(str == combo.intern()); 
	          System.out.println(0.999999999999999999999999999999999999999999999f==1f );//true*/
	          
	          String str = new String(new char[]{'a','b'});
	          str.intern();
	          String str1 ="ab";
	          	          

	    }
}

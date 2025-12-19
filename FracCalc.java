// Samhitha Nekkanty
// Period 6
// AP CSA
// Fraction Calculator Project
/*Performs basic arithmetic operations (+, -, /, *) for
 expression containing fractions, mixed and whole numbers
entered by the user and returns the fully simplified answer.*/

import java.util.*;

public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);

   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   // DO NOT CHANGE THIS METHOD!!
   public static void main(String[] args) {

      // initialize to false so that we start our loop
      boolean done = false;

      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();

         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
            // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);

            // print the result of processing the command
            System.out.println(result);
         }
      }

      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      System.out.println("Enter:");
      String input = "" + console.nextLine();
      return input;

   }

   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   // DO NOT CHANGE THIS METHOD!!!
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }

      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }

   // This method calls other methods to parse both operands, assign values
   // to the numerator, denominator and whole, and calls methods to do math 
   // Parameters: the expression the user entered as a string
   // Return: the fully simplified answer as a string
   public static String processExpression(String input) {
      // Parses both operands and assigns them to variables
      String n1 = part1(input, " ");
      String op = part2(input, " ");
      String n2 = part3(input, " ");

      // determines the type of each operand and gets specific parts of the input
      String type1 = type(n1);
      String type2 = type(n2);
      String whole1 = specificPart(n1, type1, true);
      String fraction1 = specificPart(n1, type1, false);
      String whole2 = specificPart(n2, type2, true);
      String fraction2 = specificPart(n2, type2, false);

      String numer1 = "";
      String denom1 = "";
      String numer2 = "";
      String denom2 = "";

      // These conditionals assign numerator and denominator values of both
      // operands depending on whether it's a mixed number or not
      if (type1.equals("mixed")) {
         numer1 = part1(fraction1, "/");
         denom1 = part2(fraction1, "/");
      } else {
         numer1 = getNumerator(fraction1);
         denom1 = getDenominator(fraction1);
      }
      if (type2.equals("mixed")) {
         numer2 = part1(fraction2, "/");
         denom2 = part2(fraction2, "/");
      } else {
         numer2 = getNumerator(fraction2);
         denom2 = getDenominator(fraction2);
      }
      numer1 = normalizeNumerator(numer1, denom1);
      denom1 = normalizeDenominator(denom1);
      numer2 = normalizeNumerator(numer2, denom2);
      denom2 = normalizeDenominator(denom2);

      // Converts both operands to improper fractions before passing them to methods
      // that perform calculations. This happens regardless of whether the operand is
      // a
      // fraction, mixed or whole number, because it means less redundancy
      int improperFrac1 = convertImproperNumerator(whole1, numer1, denom1);
      int improperFrac2 = convertImproperNumerator(whole2, numer2, denom2);
      int d1 = Integer.parseInt(denom1);
      int d2 = Integer.parseInt(denom2);

      if (op.equals("+") || op.equals("-")) {
         return addOrSubtractFraction(op, improperFrac1, improperFrac2, d1, d2);
      } else if (op.equals("*")) {
         return multiplyFraction(improperFrac1, improperFrac2, d1, d2);
      } else if (op.equals("/")) {
         return divideFraction(improperFrac1, improperFrac2, d1, d2);
      }
      return "";
   }

   // Checks if the user input is a mixed number, and gets the numerator
   // accordingly. If the input is not a mixed number, the getNumerator() method
   // handles it as usual
   // Parameters: The input and type of the input, return: correct numerator 
   public static String numerator(String input, String type) {
      String numer = "";
      if (type.equals("mixed")) {
         numer = part1(input, "/");

      } else {
         numer = getNumerator(input);
      }
      return numer;
   }

   // Checks in the user input is a mixed number, and gets the denominator
   // accordingly. If the input is not a mixed number, the getNumerator() method
   // handles it as usual
   // Parameters: input and type of the input, return: correct denominator
   public static String denominator(String input, String type) {
      String denom = "";
      if (type.equals("mixed")) {
         denom = part1(input, "/");

      } else {
         denom = getDenominator(input);
      }
      return denom;
   }

   // If the negative sign is in the denominator, both the numerator and
   // denominator are multiplied by -1 to make the numerator negative and 
   // denominator positive
   // Parameters: numerator and denominators, returns the normalized numerator
   public static String normalizeNumerator(String num, String den) {
      int n = Integer.parseInt(num);
      int d = Integer.parseInt(den);
      if (d < 0) {
         n *= -1;
      }
      return Integer.toString(n);

   }

   // Makes the denominator positive if it's negative. Basically the same thing as
   // the normalizeNumerator() method, but returns the denominator instead because it
   // wasn't possible for one method to return both the numerator and the denominator
   // Parameters: denominator as a string, returns: normalized denominator
   public static String normalizeDenominator(String den) {
      int d = Integer.parseInt(den);

      if (d < 0) {
         d *= -1;
      }
      return Integer.toString(d);
   }

   // Figures out which part of the user input to return depending on whether it's
   // a fraction, mixed or whole number, and whether the whole part needs to be returned or not
   // Parameters: an operand (string), the type (string), and whether the user wants
   // a whole(boolean): 
   // Returns the part as determined by parameters, or an empty string if none apply
   public static String specificPart(String input, String type, boolean whole) {
      if (type.equals("mixed")) {
         if (whole) {
            return part1(input, "_");
         } else {
            return part2(input, "_");
         }
      } else if (type.equals("whole")) {
         if (whole) {
            return input;
         } else {
            return "";
         }
      } else if (type.equals("fraction")) {
         if (whole) {
            return "0";
         } else {
            return input;
         }
      }
      return "";
   }

   // Gets and returns the numerator of the fraction, which is set to an empty
   // string by default. Takes a fraction (string) as parameters and returns the numerators
   public static String getNumerator(String fraction) {
      String numerator = "";
      // If fraction is an empty string, the value is a whole number
      if (fraction.equals("")) {
         numerator = "0";
      } else {
         numerator = part1(fraction, "/");
      }
      return numerator;
   }

   // This method converts the numerator to an improper numerator, taking parts of
   // a mixed number as parameters and returning the numerator in improper format
   public static int convertImproperNumerator(String whole, String num, String den) {
      int w = Integer.parseInt(whole);
      int n = 0;
      int d = Integer.parseInt(den);
      int sign = 1;
      // Checks to make sure there is a numerator and not an empty string before parsing
      if (!num.equals("")) {
         n = Integer.parseInt(num);
      }
      // These conditions check specifically which parts of the input (whole,
      // numerator or denominator) are negative.  Makes output is correct if negative signs are in
      // unusual places
      if (w < 0) {
         sign = -1;
      }
      if (n < 0 || d < 0) {
         sign *= -1;
      }
      // Makes all components positive to avoid errors in coversion
      w = Math.abs(w);
      n = Math.abs(n);
      d = Math.abs(d);

      // computes and returns improper numerator
      return sign * (w * d + n);
   }

   // If the output is in a improper fraction format, it is converted to a mixed number.
   // Parameters: numerator and denominator as a string  returns: The amswer is mixed number format
   public static String properFraction(int num, int den) {
      // It uses a variable called sign to make sure that negatives are also properly formatted
      int sign = 0;
      if (num < 0) {
         sign = -1;
      } else {
         sign = 1;
      }
      den = Math.abs(den);
      num = Math.abs(num);

      int whole = num / den;
      int newNum = num % den;

      int gcd = gcd(newNum, den);
      newNum /= gcd;
      den /= gcd;
      whole *= sign;
      // These conditionals make sure that only the whole number of mixed numbers are
      // negative and that only the numerator is negative if it's a fraction
      if (newNum == 0) {
         return Integer.toString(whole);
      } else if (whole == 0) {
         newNum *= sign;
         return newNum + "/" + den;
      } else {
         return whole + " " + newNum + "/" + den;
      }
   }

   // This method adds or subtracts fractions depending on whether the operator is
   // a plus or minus.  Combining these two operations into one method helps reduce redundancy
   // Parameters: The operator as a string, both numerators/denominators as ints
   // Return: the result fully simplified
   public static String addOrSubtractFraction(String op, int n1, int n2, int d1, int d2) {
      int newNum = 0;
      int newDen = 0;
      if (d1 == 0 || d2 == 0) {
         return "Sorry, denominator can't be 0";
      }
      // if the operator is a plus and the denominators are equal, the program 
      // adds the numerators/ If not, it finds common denominators
      if (d1 == d2 && op.equals("+")) {
         newNum = n1 + n2;
         newDen = d1;
      } else if (d1 != d2 && op.equals("+")) {
         newDen = d1 * d2;

         newNum = n1 * d2 + n2 * d1;
         // Does the same thing for minus sign but subtracts instead of adding
      } else if (d1 == d2 && op.equals("-")) {
         newNum = n1 - n2;
         newDen = d1;
      } else if (d1 != d2 && op.equals("-")) {
         newDen = d1 * d2;

         newNum = n1 * d2 - n2 * d1;
      }
      // calls method to reduce the answer and returns the result
      String result = simplifyFraction(newNum, newDen);
      return result;
   }

   // This method is for multiplication. If neither denominators have a value of 0,
   // the method multplies each fraction and returns the fully simplified
   // product
   // Parameters: numerators and denominators as integers, return: simplified product
   public static String multiplyFraction(int n1, int n2, int d1, int d2) {
      int newNum = 0;
      int newDen = 0;
      if (d1 != 0 && d2 != 0) {
         newNum = n1 * n2;
         newDen = d1 * d2;
      }
      else {
         return "Sorry, denominator can't be 0";
      }

      String product = simplifyFraction(newNum, newDen);
      return product;
   }

   // Divides fractions, taking the numerators and denominators as parameters and
   // returning the fully simplified quotient
   public static String divideFraction(int n1, int n2, int d1, int d2) {
      int newNum = 0;
      int newDen = 0;
      if (d1 == 0 || d2 == 0) {
         return "Sorry, denominator can't be 0";
      }
      // Because dividing by a fraction means multiplying by its reciprocal, this
      // condition checks to make sure the second numerator isn't zero. 
      // It also checks if the divisor is zero 
      if (n2 == 0) {
         return "can't divide by 0";
      } else {
         newNum = n1 * d2;
         newDen = n2 * d1;
      }
      // Prevents the negative sign from being in the denominator
      if (newDen < 0) {
         newNum *= -1;
         newDen *= -1;
      }

      String quotient = simplifyFraction(newNum, newDen);
      return quotient;

   }

   // This method reduces each answer using the greatest common divisor
   // and returning the simplified version
   // Parameters: numerator and denomiator as ints, returns simplified
   // answer as string
   public static String simplifyFraction(int newNum, int newDen) {
      int gcd = gcd(newNum, newDen);
      newNum /= gcd;
      newDen /= gcd;

      // If the numerator is divisible by the denominator the result is a whole number
      if (newNum % newDen == 0) {
         return Integer.toString(newNum / newDen);

         // If the absolute value of the numerator is greater than but not divisible by
         // the denomintator,
         // proper fraction method is used
      } else if (Math.abs(newNum) > newDen && newNum % newDen != 0) {
         return properFraction(newNum, newDen);
      }
      String result = newNum + "/" + newDen;
      return result;
   }

   // Computes the gcd using Euclid's algorithm
   // Parameters: the numerator and denominator. Returns the gcd
   public static int gcd(int num, int den) {
      num = Math.abs(num);
      den = Math.abs(den);
      while (den != 0) {
         int temp = num % den;
         num = den;
         den = temp;
      }
      return num;
   }

   // Gets and returns the denominator of the fraction, which is set to an empty
   // string by default
   // Parameters: the fraction as a string. Returns the denominator
   public static String getDenominator(String fraction) {
      String denominator = "";
      // If fraction is an empty string, it means the value is a whole number, all of
      // which
      // have denominators of 1
      if (fraction.equals("")) {
         denominator = "1";

         // Otherwise, the denominator is the part of fraction starting from the slash to
         // the
         // end of the string. Receives the value using the part2() method
      } else {
         denominator = part2(fraction, "/");

      }
      return denominator;
   }

   // This method is used to determine the type of each operand and it
   // returns the type (fraction, mixed or whole number)
   // Parameters are the operand, returns the  type of number as a string
   public static String type(String input) {
      boolean underscore = input.contains("_");
      boolean slash = input.contains("/");
      if (underscore && slash) {
         return "mixed";
      } else if (slash) {
         return "fraction";
      } else if (!underscore && !slash) {
         return "whole";
      }
      return "whole";
   }

   // Returns the first part of the user input, from the beginning to the
   // first occurence of the seperator using substring
   // Parameters, the input and seperator, return: first part
   public static String part1(String input, String sep) {
      int in = input.indexOf(sep);
      String first = "";
      // If the seperator isn't found, it returns the entire string
      if (in == -1) {
         first = input;
      } else {

         first = input.substring(0, in);
      }
      return first;
   }

   // Returns the part of a string between two instances of a seperator
   // (the parameter sep) by using string methods, returns an empty string if there
   // are no occurences of the seperator
   // Parameters: input and seperator, return: second part
   public static String part2(String input, String sep) {
      int in1 = input.indexOf(sep);
      if (in1 == -1) {
         return "";
      }
      String nextSection = input.substring(in1 + 1);
      int in2 = nextSection.indexOf(sep);

      String second = "";
      if (in2 == -1) {

         second = nextSection.substring(0);
      } else {
         second = nextSection.substring(0, in2);
      }
      return second;
   }

   // This method returns a third part of an input which is the string after the
   // second occurence of the seperator in input
   // Parameters: input and seperator, return: third part
   public static String part3(String input, String sep) {

      int in1 = input.indexOf(sep);
      String nextSection = input.substring(in1 + 1);
      int in2 = nextSection.indexOf(sep);
      String third = nextSection.substring(in2 + 1);
      return third;
   }

   // This method provides help to the user if they type in help
   public static String provideHelp() {

      String help = "Enter an expression with two numbers seperated by spaces and an operator\n";
      help += "They can be fractions, whole or mixed numbers. program will evaluate expressions with +, -, *, / operators. ";
      help += "\nseperate fractions with a slash (/) and mixed numbers with an underscore (_). Lock in!";
      return help;
   }
}
//Euclidean algorithm: https://en.wikipedia.org/wiki/Euclidean_algorithm
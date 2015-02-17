package com.basicsetup.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.content.Context;
import android.util.TypedValue;

public class MathUtil {

	public static double round(double val, int roundTo) {

		/*
		 * double multiPlier = java.lang.Math.pow( 10, roundTo);
		 * 
		 * float fl = (float) (java.lang.Math.round(val * multiPlier) /
		 * multiPlier);
		 * 
		 * return fl;
		 */
		
		BigDecimal bigDecimal = new BigDecimal(Double.toString(val));
		try{		
			bigDecimal = bigDecimal.setScale(	roundTo,
											BigDecimal.ROUND_UNNECESSARY);
		}catch (ArithmeticException e) {
			e.printStackTrace();
			
			bigDecimal = bigDecimal.setScale(	roundTo,
												BigDecimal.ROUND_HALF_EVEN);
		}
		return bigDecimal.doubleValue();

	}
	
	public static double roundUsingDecimalFormat(double value,String decimalFormaterString){
		DecimalFormat format = new DecimalFormat(decimalFormaterString);
		return Double.parseDouble(format.format(value));
	}
	public static int convertDip2Pixels(Context context, int dip) {
	    return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}
}

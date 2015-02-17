package com.basicsetup.webservices.parser;



public interface IParser<T>{
	
	T parse(String str);
}

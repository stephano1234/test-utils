package br.com.contmatic.testes.utilidades;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyClassTeste {
	
	public static void main(String[] args) {
		Map proxyInstance = (Map) Proxy.newProxyInstance(null, new Class[] {Map.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(method.getName());
				return null;
			}
		});
		proxyInstance.put("ff", "aa");
		proxyInstance.entrySet();
		//proxyInstance.isEmpty();
		//proxyInstance.putAll(proxyInstance);
		
		System.out.println(FuncoesRandomicas.generateStringBySize(5));
		System.out.println(FuncoesRandomicas.generateStringBySizeAndRegex(5, "[A-Z]"));
		System.out.println(FuncoesRandomicas.generateStringBySizeAndRegexWithOneCharByRegex(5, "\\d", "\\w"));
		System.out.println(FuncoesRandomicas.generateStringBySizeAndRegexWithSeparator(5, "[A-Z]", " "));
	}

}

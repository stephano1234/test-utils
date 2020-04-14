package br.com.contmatic.testes.utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.google.common.base.Preconditions;

/**
 * The Class Verificadores.
 */
public class Verificadores {

	private static final String MENSAGEM_ERRO_NAO_INFORMADA = "A mensagem do erro procurado deve ser informada.";

	/** The Constant PARAM_NULO. */
	private static final String PARAM_NULO = "O objeto testado não pode ser nulo.";

	/** The validator. */
	private static Validator validator;

	/** The factory. */
	private static ValidatorFactory factory;

	/**
	 * Instantiates a new verificadores.
	 */
	private Verificadores() {
	}

	/**
	 * Verifica erro.
	 *
	 * @param objetoTestado the objeto testado
	 * @param mensagem      the mensagem
	 * @return true, if successful
	 */
	public static boolean procuraViolacao(Object objetoTestado, String mensagem, Class<?> groupClass) {
		Preconditions.checkNotNull(objetoTestado, PARAM_NULO);
		Preconditions.checkNotNull(mensagem, MENSAGEM_ERRO_NAO_INFORMADA);
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		boolean possuiErro = false;
		Set<ConstraintViolation<Object>> violacoes = validator.validate(objetoTestado, groupClass);
		for (ConstraintViolation<Object> violacao : violacoes) {
			if (violacao.getMessage().equals(mensagem)) {
				possuiErro = true;
			}
		}
		return possuiErro;
	}

	/**
	 * Procura algum erro.
	 *
	 * @param objetoTestado the objeto testado
	 * @return true, if successful
	 */
	public static boolean procuraQualquerViolacao(Object objetoTestado, Class<?> groupClass) {
		Preconditions.checkNotNull(objetoTestado, PARAM_NULO);
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violacoes = validator.validate(objetoTestado, groupClass);
		return !violacoes.isEmpty();
	}

	public static boolean verificaEncapsulamentos(Class<?> classe) {
		Object objeto = classeNewer(classe);
		List<Field> campos = Arrays.asList(classe.getDeclaredFields());
		List<Method> metodosSet = metodosSetters(classe, campos);
		List<Method> metodosGet = metodosGetters(classe, campos);
		Object[] valores = geraValoresCampos(campos);
		if (metodosGet.size() == metodosSet.size() && metodosGet.size() == campos.size()) {
			for (int i = 0; i < valores.length; i++) {
				if (!verificaDeterminadoGetSetCampo(objeto, campos, metodosSet, metodosGet, valores, i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private static boolean verificaDeterminadoGetSetCampo(Object objeto, List<Field> campos, List<Method> metodosSet,
			List<Method> metodosGet, Object[] valores, int i) {
		try {
			metodosSet.get(i).invoke(objeto, campos.get(i).getType().cast(valores[i]));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		try {
			if (valores[i] != metodosGet.get(i).invoke(objeto)) {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return true;
	}

	private static Object classeNewer(Class<?> classe) {
		if (classe.isInterface()) {
			return classe.cast(Proxy.newProxyInstance(null, new Class[] { classe }, new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return null;
				}
			}));
		}
		try {
			return classe.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	private static List<Method> metodosSetters(Class<?> classe, List<Field> campos) {
		List<Method> metodosSet = new ArrayList<>();
		StringBuilder nomeMetodoSetter = new StringBuilder();
		for (Field campo : campos) {
			for (Method metodo : classe.getDeclaredMethods()) {
				nomeMetodoSetter.append("set").append(campo.getName());
				if (metodo.getName().equalsIgnoreCase(nomeMetodoSetter.toString())) {
					metodosSet.add(metodo);
				}
				nomeMetodoSetter.setLength(0);
			}
		}
		return metodosSet;
	}

	private static List<Method> metodosGetters(Class<?> classe, List<Field> campos) {
		List<Method> metodosGet = new ArrayList<>();
		StringBuilder nomeMetodoGetter = new StringBuilder();
		for (Field campo : campos) {
			for (Method metodo : classe.getDeclaredMethods()) {
				nomeMetodoGetter.append("get").append(campo.getName());
				if (metodo.getName().equalsIgnoreCase(nomeMetodoGetter.toString())) {
					metodosGet.add(metodo);
				}
				nomeMetodoGetter.setLength(0);
			}
		}
		return metodosGet;
	}

	private static Object[] geraValoresCampos(List<Field> campos) {
		Object[] valores = new Object[campos.size()];
		int i = 0;
		for (Field campo : campos) {
			if (campo.getType().isPrimitive()) {
				geraValoresCamposPrimitivos(valores, i, campo.getType());
				i++;
			}
			if (campo.getType().isEnum()) {
				geraValoresCamposEnum(valores, i, campo.getType());
				i++;
			}
			if (!campo.getType().isEnum() && !campo.getType().isPrimitive()) {
				valores[i] = classeNewer(campo.getType());
				i++;
			}
		}
		return valores;
	}

	private static void geraValoresCamposPrimitivos(Object[] valores, int i, Class<?> classeCampo) {
		switch (classeCampo.getTypeName()) {
		case "boolean":
			valores[i] = false;
			break;
		case "int":
			valores[i] = 0;
			break;
		case "long":
			valores[i] = 0l;
			break;
		case "float":
			valores[i] = 0.0f;
			break;
		case "double":
			valores[i] = 0.0;			
			break;
		default:
			break;
		}
	}

	private static void geraValoresCamposEnum(Object[] valores, int i, Class<?> classeCampo) {
		valores[i] = classeCampo.getEnumConstants()[0];
	}

}

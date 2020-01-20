package br.com.contmatic.testes.utilidades;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;

import com.google.common.base.Preconditions;
import com.mifmif.common.regex.Generex;

/**
 * The Class FuncoesRandomicas.
 */
public class FuncoesRandomicas {

	/**
	 * Instantiates a new funcoes randomicas.
	 */
	private FuncoesRandomicas() {
	}
	
	/**
	 * Somente caractere.
	 *
	 * @param tamanho the tamanho
	 * @param expressaoRegular the expressao regular
	 * @return the string
	 */
	public static String generateStringBySizeAndRegex(int tamanho, String expressaoRegular) {
		Preconditions.checkArgument(tamanho >= 0,
				"O tamanho da string gerada não pode ser um número inteiro negativo.");
		Preconditions.checkNotNull(expressaoRegular, "A expressão regular deve ser informada.");
		return new Generex(expressaoRegular + "{" + tamanho + "}").random();
	}

	/**
	 * Generate string by size and regex with separator.
	 *
	 * @param tamanho the tamanho
	 * @param expressaoRegular the expressao regular
	 * @param separador the separador
	 * @return the string
	 */
	public static String generateStringBySizeAndRegexWithSeparator(int tamanho, String expressaoRegular, String separador) {
		final StringBuilder string = new StringBuilder(generateStringBySizeAndRegex(tamanho, expressaoRegular));
		final Set<Integer> posicoesSeparadores = new HashSet<>();
		final int quantidadeSeparadores = RandomUtils.nextInt(0, tamanho);
		while (posicoesSeparadores.size() < quantidadeSeparadores) {
			posicoesSeparadores.add(RandomUtils.nextInt(1, tamanho));
		}
		final Integer[] posicoesOrdenadasSeparadores = posicoesSeparadores.toArray(new Integer[0]);
		Arrays.sort(posicoesOrdenadasSeparadores);
		for (int i = posicoesOrdenadasSeparadores.length - 1; i >= 0; i--) {
			string.insert(posicoesOrdenadasSeparadores[i], separador);
		}
		return string.toString();
	}

	/**
	 * Apenas um caractere.
	 *
	 * @param tamanho the tamanho
	 * @param regexCaractere the regex caractere
	 * @param regexCaracteres the regex caracteres
	 * @return the string
	 */
	public static String generateStringBySizeAndRegexWithOneCharByRegex(int tamanho, String regexCaractere, String regexCaracteres) {
		Preconditions.checkArgument(tamanho >= 1,
				"O tamanho da string gerada por apenasUmCaractere deve ser maior ou igual a um.");
		Preconditions.checkNotNull(regexCaractere, "A expressão regular do caractere único deve ser informada.");
		Preconditions.checkNotNull(regexCaracteres, "A expressão regular dos outros caracteres deve ser informada.");
		int posicaoCaractere = RandomUtils.nextInt(0, tamanho);
		return generateStringBySizeAndRegex(posicaoCaractere, regexCaracteres) + generateStringBySizeAndRegex(1, regexCaractere)
				+ generateStringBySizeAndRegex((tamanho - 1) - posicaoCaractere, regexCaracteres);
	}

	public static String generateStringBySize(int tamanho) {
		return new Generex("." + "{" + tamanho + "}").random();
	}

	/**
	 * Email aleatorio.
	 *
	 * @return the string
	 */
	public static String emailAleatorio() {
		StringBuilder emailRandom = new StringBuilder();
		emailRandom.append(generateStringBySizeAndRegex(1, ConstantesTesteString.LETRA_MINUSCULA_NUMERAL));
		emailRandom.append(generateStringBySizeAndRegex(RandomUtils.nextInt(ConstantesTesteNumericas.INCLUI_STRING_VAZIO, 29 + 1),
				"([a-z]|[0-9]|[\\.]|[_]|[-])"));
		if (emailRandom.substring(emailRandom.length() - 1, emailRandom.length()).matches("[-_\\.]")) {
			emailRandom.deleteCharAt(emailRandom.length() - 1);
			emailRandom.append(generateStringBySizeAndRegex(1, ConstantesTesteString.LETRA_MINUSCULA_NUMERAL));
		}
		emailRandom.append("@");
		emailRandom.append(generateStringBySizeAndRegex(1, ConstantesTesteString.LETRA_MINUSCULA_NUMERAL));
		emailRandom.append(generateStringBySizeAndRegex(RandomUtils.nextInt(ConstantesTesteNumericas.INCLUI_STRING_VAZIO, 19 + 1),
				"([a-z]|[0-9]|[\\.]|[-])"));
		if (emailRandom.substring(emailRandom.length() - 1, emailRandom.length()).matches("[-_\\.]")) {
			emailRandom.deleteCharAt(emailRandom.length() - 1);
			emailRandom.append(generateStringBySizeAndRegex(1, ConstantesTesteString.LETRA_MINUSCULA_NUMERAL));
		}
		emailRandom.append(".");
		emailRandom.append(generateStringBySizeAndRegex(RandomUtils.nextInt(2, 6 + 1), "[a-z]"));
		return emailRandom.toString();
	}

	/**
	 * Cpf valido.
	 *
	 * @return the string
	 */
	public static String cpfValido() {
		StringBuilder cpfRandom = new StringBuilder();
		cpfRandom.append(generateStringBySizeAndRegex(9, ConstantesTesteString.APENAS_NUMERAL));
		while (cpfRandom.charAt(7) == cpfRandom.charAt(8)) {
			cpfRandom.deleteCharAt(8);
			cpfRandom.append(generateStringBySizeAndRegex(1, ConstantesTesteString.APENAS_NUMERAL));
		}
		int soma = 0;
		for (int i = 0; i < ConstantesTesteNumericas.CPF - 2; i++) {
			soma += Integer.parseInt(cpfRandom.substring(i, i + 1)) * (10 - i);
		}
		cpfRandom.append((((11 - (soma % 11)) % 11) % 10));
		soma = 0;
		for (int i = 0; i < ConstantesTesteNumericas.CPF - 1; i++) {
			soma += Integer.parseInt(cpfRandom.substring(i, i + 1)) * (11 - i);
		}
		cpfRandom.append((((11 - (soma % 11)) % 11) % 10));
		return cpfRandom.toString();
	}

	/**
	 * Cnpj valido.
	 *
	 * @return the string
	 */
	public static String cnpjValido() {
		StringBuilder cnpjRandom = new StringBuilder();
		cnpjRandom.append(generateStringBySizeAndRegex(8, ConstantesTesteString.APENAS_NUMERAL));
		cnpjRandom.append("0001");
		int[] multiplicadores = { 6, 5, 4, 3, 2, 9, 8, 7 };
		int soma = 0;
		for (int i = 0; i < ConstantesTesteNumericas.CNPJ - 2; i++) {
			soma += Integer.parseInt(cnpjRandom.substring(i, i + 1)) * multiplicadores[(i + 1) % 8];
		}
		cnpjRandom.append((((11 - (soma % 11)) % 11) % 10));
		soma = 0;
		for (int i = 0; i < ConstantesTesteNumericas.CNPJ - 1; i++) {
			soma += Integer.parseInt(cnpjRandom.substring(i, i + 1)) * multiplicadores[i % 8];
		}
		cnpjRandom.append((((11 - (soma % 11)) % 11) % 10));
		return cnpjRandom.toString();
	}

	/**
	 * Cpf invalido.
	 *
	 * @param posicaoDigitoVerificador the posicao digito verificador
	 * @return the string
	 */
	public static String cpfInvalido(int posicaoDigitoVerificador) {
		StringBuilder cpfInvalidoRandom = new StringBuilder(cpfValido());
		Integer digitoVerificadorValido = Integer
				.parseInt(cpfInvalidoRandom.substring(posicaoDigitoVerificador, posicaoDigitoVerificador + 1));
		Integer digitoVerificadorInvalido = RandomUtils.nextInt(0, 10);
		while (digitoVerificadorValido.equals(digitoVerificadorInvalido)) {
			digitoVerificadorInvalido = RandomUtils.nextInt(0, 10);
		}
		cpfInvalidoRandom.deleteCharAt(posicaoDigitoVerificador);
		cpfInvalidoRandom.insert(posicaoDigitoVerificador, digitoVerificadorInvalido.toString());
		return cpfInvalidoRandom.toString();
	}

	/**
	 * Cnpj invalido.
	 *
	 * @param posicaoDigitoVerificador the posicao digito verificador
	 * @return the string
	 */
	public static String cnpjInvalido(int posicaoDigitoVerificador) {
		StringBuilder cnpjInvalidoRandom = new StringBuilder(cnpjValido());
		Integer digitoVerificadorValido = Integer
				.parseInt(cnpjInvalidoRandom.substring(posicaoDigitoVerificador, posicaoDigitoVerificador + 1));
		Integer digitoVerificadorInvalido = RandomUtils.nextInt(0, 10);
		while (digitoVerificadorValido.equals(digitoVerificadorInvalido)) {
			digitoVerificadorInvalido = RandomUtils.nextInt(0, 10);
		}
		cnpjInvalidoRandom.deleteCharAt(posicaoDigitoVerificador);
		cnpjInvalidoRandom.insert(posicaoDigitoVerificador, digitoVerificadorInvalido.toString());
		return cnpjInvalidoRandom.toString();
	}

}

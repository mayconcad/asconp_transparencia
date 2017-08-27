package br.com.asconp.publitec.exceptions;

public class IPServidorDBException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5626569051979952175L;

	private String mensagem = "Não foi possível realizar a conexão, confira o número do ip do seu banco ";

	public IPServidorDBException(String banco) {
		this.mensagem = String.format("%s%s", this.mensagem, banco);
	}

	public String getMensagem() {
		return mensagem;
	}
}

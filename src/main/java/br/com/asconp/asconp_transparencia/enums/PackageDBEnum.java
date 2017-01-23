package br.com.asconp.asconp_transparencia.enums;

public enum PackageDBEnum {

	MYSQL_DRIVER(0, "com.mysql.jdbc.Driver"), POSTGRES_DRIVER(1,
			"org.postgresql.Driver");

	private String descricao;
	private int index;

	private PackageDBEnum(int index, String descricao) {
		this.descricao = descricao;
		this.index = index;
	}

	public String formatPrefixConnection(String ip, String porta) {
		switch (index) {
		case 0:
			return String.format("jdbc:mysql://%s:%s/sistemavendas", ip, porta);
		case 1:
			return String.format("jdbc:postgresql://%s:%s/conexao_jdbc", ip, porta);
		default:
			return String.format("jdbc:postgresql://%s:%s", ip, porta);
		}
	}

	public String getDescricao() {
		return descricao;
	}

}

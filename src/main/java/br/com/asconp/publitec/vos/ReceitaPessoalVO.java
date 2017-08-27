package br.com.asconp.publitec.vos;



public class ReceitaPessoalVO extends BaseVO {
	
	//private final long serialVersionUID = 2878321609647514057L;

	private long id;

	private String cpf;
	
	private String nome;
	
	private String lotacao;
	
	private String cargo;
	
	private String remuneracao;
	
	//private String cpfLink;
	
	public ReceitaPessoalVO() {
		// TODO Auto-generated constructor stub
	}
		

	public ReceitaPessoalVO(long id, String cpf, String nome, String lotacao,
			String cargo, String remuneracao) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.nome = nome;
		this.lotacao = lotacao;
		this.cargo = cargo;
		this.remuneracao = remuneracao;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	public String getCpfLink() {
//		return cpfLink;
//	}
//
//	public void setCpfLink(String cpfLink) {
//		this.cpfLink = cpfLink;
//	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLotacao() {
		return lotacao;
	}

	public void setLotacao(String lotacao) {
		this.lotacao = lotacao;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getRemuneracao() {
		return remuneracao;
	}

	public void setRemuneracao(String remuneracao) {
		this.remuneracao = remuneracao;
	}


//	public long getSerialversionuid() {
//		return serialVersionUID;
//	}

	
}

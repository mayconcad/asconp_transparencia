package br.com.asconp.asconp_transparencia.vos;

public class CategoryReportVO extends BaseVO {

	private static final long serialVersionUID = 7472709995708741629L;

	private String name;

	public CategoryReportVO() {}

	public CategoryReportVO( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

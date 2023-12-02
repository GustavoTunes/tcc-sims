package projeto.sims.model;

public enum UsuarioFuncao {

	ADMIN(0),
	USER(1);
	
	private int role;
	
	UsuarioFuncao(int role) {
		this.role = role;
	}

	public int getRole() {
		return role;
	}
	
	
	
}
